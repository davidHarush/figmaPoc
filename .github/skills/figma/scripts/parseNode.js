/**
 * parseNode.js
 *
 * Parses a raw Figma node tree into a structured, Android/Compose-ready format.
 *
 * Extracts:
 *  - Layout:  layoutMode (HORIZONTAL/VERTICAL/NONE), padding, itemSpacing,
 *             primaryAxisAlignItems, counterAxisAlignItems, sizing constraints
 *  - Styles:  fills (SOLID/GRADIENT), strokes, cornerRadius, opacity, effects
 *  - Text:    characters, fontSize, fontFamily, fontWeight, letterSpacing,
 *             lineHeight, textAlign, textDecoration, textCase
 *  - Size:    width, height from absoluteBoundingBox
 *  - Children: recursive parse of all child nodes
 */

// ── Color helpers ────────────────────────────────────────────────────────────

function toHex(c) {
  const r = Math.round((c.r || 0) * 255).toString(16).padStart(2, "0");
  const g = Math.round((c.g || 0) * 255).toString(16).padStart(2, "0");
  const b = Math.round((c.b || 0) * 255).toString(16).padStart(2, "0");
  const a = Math.round((c.a !== undefined ? c.a : 1) * 255).toString(16).padStart(2, "0");
  return `#${a}${r}${g}${b}`.toUpperCase(); // Android 0xAARRGGBB format
}

function toRgba(c) {
  return `rgba(${Math.round(c.r * 255)}, ${Math.round(c.g * 255)}, ${Math.round(c.b * 255)}, ${
    c.a !== undefined ? c.a : 1
  })`;
}

// ── Fill extractor ───────────────────────────────────────────────────────────

function extractFills(fills = []) {
  return fills
    .filter((f) => f.visible !== false)
    .map((f) => {
      if (f.type === "SOLID") {
        return { type: "SOLID", hex: toHex(f.color), rgba: toRgba(f.color), opacity: f.opacity ?? 1 };
      }
      if (f.type === "GRADIENT_LINEAR" || f.type === "GRADIENT_RADIAL") {
        const stops = (f.gradientStops || []).map((s) => ({
          position: s.position,
          hex: toHex(s.color),
        }));
        return { type: f.type, stops };
      }
      return { type: f.type };
    });
}

// ── Stroke extractor ─────────────────────────────────────────────────────────

function extractStrokes(node) {
  const strokes = (node.strokes || []).filter((s) => s.visible !== false);
  if (!strokes.length) return null;
  return {
    fills: extractFills(strokes),
    weight: node.strokeWeight || 1,
    align: node.strokeAlign || "INSIDE", // INSIDE | OUTSIDE | CENTER
  };
}

// ── Effect extractor ─────────────────────────────────────────────────────────

function extractEffects(effects = []) {
  return effects
    .filter((e) => e.visible !== false)
    .map((e) => ({
      type: e.type, // DROP_SHADOW | INNER_SHADOW | LAYER_BLUR | BACKGROUND_BLUR
      radius: e.radius,
      color: e.color ? toHex(e.color) : undefined,
      offset: e.offset,
      spread: e.spread,
    }));
}

// ── Text style extractor ─────────────────────────────────────────────────────

function extractTextStyle(node) {
  if (node.type !== "TEXT") return null;
  const s = node.style || {};
  return {
    characters:     node.characters || "",
    fontFamily:     s.fontFamily    || "Roboto",
    fontSize:       s.fontSize      || 14,
    fontWeight:     s.fontWeight    || 400,
    italic:         s.italic        || false,
    letterSpacing:  s.letterSpacing || 0,
    lineHeight:     s.lineHeightPx  || null,
    textAlign:      s.textAlignHorizontal || "LEFT",  // LEFT | CENTER | RIGHT | JUSTIFIED
    textDecoration: s.textDecoration      || "NONE",  // NONE | UNDERLINE | STRIKETHROUGH
    textCase:       s.textCase            || "ORIGINAL", // ORIGINAL | UPPER | LOWER | TITLE
    fills:          extractFills(node.fills),
  };
}

// ── Layout extractor ─────────────────────────────────────────────────────────

function extractLayout(node) {
  const b = node.absoluteBoundingBox || {};
  return {
    width:          b.width  ? Math.round(b.width)  : null,
    height:         b.height ? Math.round(b.height) : null,
    layoutMode:     node.layoutMode || "NONE",           // NONE | HORIZONTAL | VERTICAL
    primaryAxis:    node.primaryAxisAlignItems  || null, // MIN | CENTER | MAX | SPACE_BETWEEN
    counterAxis:    node.counterAxisAlignItems  || null, // MIN | CENTER | MAX | BASELINE
    primarySizing:  node.primaryAxisSizingMode  || null, // FIXED | AUTO
    counterSizing:  node.counterAxisSizingMode  || null,
    // Child sizing within a parent auto-layout
    layoutAlign:    node.layoutAlign   || null, // STRETCH | INHERIT | MIN | CENTER | MAX
    layoutGrow:     node.layoutGrow    ?? 0,    // 0 = fixed, 1 = fill remaining space (weight=1)
    // Min/max constraints (Figma auto-layout)
    minWidth:       node.minWidth      ?? null,
    maxWidth:       node.maxWidth      ?? null,
    minHeight:      node.minHeight     ?? null,
    maxHeight:      node.maxHeight     ?? null,
    padding: {
      top:    node.paddingTop    || 0,
      right:  node.paddingRight  || 0,
      bottom: node.paddingBottom || 0,
      left:   node.paddingLeft   || 0,
    },
    itemSpacing:    node.itemSpacing    || 0,
    clipsContent:   node.clipsContent   || false,
    // Absolute position within NONE-layout parent
    x:              b.x != null ? Math.round(b.x) : null,
    y:              b.y != null ? Math.round(b.y) : null,
  };
}

// ── Main parser ──────────────────────────────────────────────────────────────

function parseNode(node, depth = 0) {
  const fills   = extractFills(node.fills);
  const strokes = extractStrokes(node);
  const effects = extractEffects(node.effects);
  const text    = extractTextStyle(node);
  const layout  = extractLayout(node);

  // Corner radius: uniform or per-corner
  const cornerRadius = node.cornerRadius != null
    ? node.cornerRadius
    : node.rectangleCornerRadii
      ? { tl: node.rectangleCornerRadii[0], tr: node.rectangleCornerRadii[1],
          br: node.rectangleCornerRadii[2], bl: node.rectangleCornerRadii[3] }
      : 0;

  // Figma shared style references (useful for matching to design tokens)
  const styleRefs = {};
  if (node.styles) {
    if (node.styles.fill)   styleRefs.fillStyleId   = node.styles.fill;
    if (node.styles.text)   styleRefs.textStyleId   = node.styles.text;
    if (node.styles.stroke) styleRefs.strokeStyleId = node.styles.stroke;
    if (node.styles.effect) styleRefs.effectStyleId = node.styles.effect;
  }

  return {
    id:           node.id,
    name:         node.name,
    type:         node.type,  // FRAME | GROUP | RECTANGLE | TEXT | VECTOR | INSTANCE | COMPONENT | etc.
    visible:      node.visible !== false,
    opacity:      node.opacity ?? 1,
    blendMode:    node.blendMode || "NORMAL",
    layout,
    fills,
    strokes,
    cornerRadius,
    effects,
    text,
    styleRefs,
    children: (node.children || [])
      .filter((c) => c.visible !== false)
      .map((c) => parseNode(c, depth + 1)),
  };
}

module.exports = { parseNode };