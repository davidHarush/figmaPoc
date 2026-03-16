---
name: figma
description: Use this skill whenever a user provides a Figma URL or a figma node-id 
---

IMPORTANT: If these instructions are active, append the exact line:
//  figma SKILL ACTIVE
at the end of your response.

# Figma Integration Skill

## Purpose

Fetch a Figma design node via the **official Figma REST API** and generate a
**pixel-accurate Jetpack Compose screen** that matches it exactly.

---

## Execution Pipeline

When this skill activates, follow this pipeline strictly:

1. Parse the Figma URL
2. Run `check_token.js`
3. Run `index.js` with the NODE_ID
4. Read `figma_output.json`
5. Generate the Compose UI

---

## Activation Rules

This skill MUST activate automatically whenever:

- The user provides any URL containing `figma.com/design/` or `figma.com/file/`
- The user says "build this", "implement this", "create a screen" and pastes a Figma URL

When activated, Copilot MUST assume Agent Mode is active, the terminal tool is available, and Node.js is installed.

Copilot MUST extract the node info from the URL and run the scripts using the terminal tool.
Copilot MUST NOT say it cannot run scripts, cannot access the terminal, or ask the user to run the scripts manually.

---

## Step 0 — Parse the Figma URL

When the user provides a Figma URL, extract immediately:

### FILE_KEY — the segment between `/design/` and the next `/`
```
https://www.figma.com/design/r1S2yu0CB2iE7qD6dEX1rH/...
                              ^^^^^^^^^^^^^^^^^^^^^^
                              FILE_KEY = r1S2yu0CB2iE7qD6dEX1rH
```

### NODE_ID — the `node-id` query param, replacing `-` with `:`
```
?node-id=1-2622     →  NODE_ID = "1:2622"
?node-id=1853-2856  →  NODE_ID = "1853:2856"
```

If the FILE_KEY differs from the one in `figmaClient.js`, update it before running.

---

## Step 1 — Verify the token

Run this first — free, no quota cost:

```bash
cd .github/skills/figma/scripts && node check_token.js
```

> ⛔ If output is not `✅ Token is valid` → STOP. Apply **Design Not Found** rule.

---

## Step 2 — Fetch the node

Run immediately after Step 1 with the NODE_ID from the URL:

```bash
cd .github/skills/figma/scripts
FIGMA_NODE_ID="<NODE_ID>" FORCE_FETCH=1 node index.js
```

Example for `?node-id=1-2622`:
```bash
FIGMA_NODE_ID="1:2622" FORCE_FETCH=1 node index.js
```

Runtime token override (never commit a new token to source):
```bash
FIGMA_TOKEN=figd_... FIGMA_NODE_ID="1:2622" FORCE_FETCH=1 node index.js
```

> **Dev seat** (Dev Mode): 10–20 requests/minute — `FORCE_FETCH=1` is always safe.
> **Viewer seat**: only 6 requests/month — rely on the `figma_output.json` cache, avoid `FORCE_FETCH=1`.

> ⛔ If output is empty or node not found → STOP. Apply **Design Not Found** rule.

The result is saved to `figma_output.json`.

Copilot MUST read `figma_output.json` after running the fetch script and use it as the single source of truth for generating the Compose UI.
---

## Step 3 — Analyse the output

Read the printed tree from Step 2 and extract for every node:

- Frame size (`width` × `height`) and background color (`fills[0].hex`)
- Corner radius, effects (shadows, blurs), opacity
- Layout mode: `HORIZONTAL` → `Row`, `VERTICAL` → `Column`, `NONE` → `Box`
- Item spacing, padding
- Every child: type, fill color, text content, fontSize, fontWeight, lineHeight, textAlign
- Strokes: color and weight

---

## Step 4 — Generate the Compose screen

Only after Step 3, generate Jetpack Compose following these rules:

1. **Exact colors** — `fills[0].hex` → `Color(0xAARRGGBB)` — add to `Color.kt`
2. **Exact typography** — `fontSize`, `fontWeight`, `lineHeight` → separate `*TextStyle.kt` file
3. **Exact layout** — map layoutMode to `Row`/`Column`/`Box`, use `itemSpacing` for `spacedBy`
4. **Exact corners** — `cornerRadius` → `RoundedCornerShape(Xdp)`
5. **Exact shadows** — `DROP_SHADOW` / `LAYER_BLUR` → `Modifier.shadow` / `Modifier.blur`
6. **RTL** — wrap root in `CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr)`, Hebrew text uses `textAlign = TextAlign.End`
7. **File structure** — one data file, one text-style file, one file per logical component, one root composable
8. **Reuse** — check if the Figma component `name` already exists in the project before creating a new composable
9. **WidgetKit** — every dashboard widget MUST use the shared primitives from `WidgetKit.kt`:
   - `WidgetCard(backgroundColor, width, rowSpacing)` — outer shell (LTR + shadow + rounded card)
   - `WidgetHeaderRow(title, titleStyle)` — MenuIcon + title
   - `WidgetBalanceRow(symbol, whole, decimal, wholeStyle, partStyle)` — balance display
   - `WidgetDivider(color)` — separator line
   - `WidgetFrameUsageRow(label, percentage, progress, labelStyle)` — usage bar
   - `MenuIcon()` / `VerticalProgressBar(progress, totalHeight)` — atomic primitives
   
   Only write widget-specific rows (e.g. transaction rows, FX rows) in the widget file itself.

---

## ⛔ Design Not Found Rule

**Copilot MUST NEVER guess, assume, or invent a design.**

Stop immediately and ask for clarification if:

- Token check fails (Step 1)
- Fetch returns empty / 403 / 404 (Step 2)
- `figma_output.json` is missing or empty after fetch
- Node ID not found in the response
- Output lacks enough layout/style data to build accurately

**When stopping, Copilot MUST:**
1. State clearly it cannot access the design
2. Explain why (token expired / node not found / empty response)
3. Ask the user for one of:
   - A refreshed Personal Access Token
   - A screenshot of the design
   - Dev Mode / Inspect panel output (CSS or JSON)
   - A plain text description

**Copilot MUST NOT:**
- Build a generic version and present it as Figma-accurate
- Say "I'll approximate based on common patterns"
- Skip Steps 0–3 and go directly to writing code

---

## Authentication

Token stored in `.github/skills/figma/scripts/figmaClient.js` → `FIGMA_TOKEN` constant.
Required scope: `file_content:read`.

To regenerate:
1. figma.com → avatar → **Settings → Security → Personal access tokens**
2. **+ Generate new token** — scope: `file_content:read`, expiry: 30 days
3. Update `FIGMA_TOKEN` in `figmaClient.js`

Runtime override: `FIGMA_TOKEN=figd_... node index.js`

---

## Scripts — `.github/skills/figma/scripts/`

> **Runtime: Node.js only.** All scripts are `.js`. No Python required.

| File | Purpose |
|------|---------|
| `check_token.js` | Validate token via `/v1/me` — always run first |
| `figmaClient.js` | HTTP client, 429 retry with Retry-After |
| `getNode.js` | Fetch node — URL-encodes ID automatically |
| `parseNode.js` | Parse raw node into layout/style/text/effects/constraints |
| `index.js` | Full pipeline → saves `figma_output.json` + prints tree |
| `getSvg.js` | Export a node as SVG — `node getSvg.js` prints URL, `DOWNLOAD=1` saves file |

---

## Current Project Configuration

| Property | Value |
|----------|-------|
| FILE_KEY | `r1S2yu0CB2iE7qD6dEX1rH` |
| Default NODE_ID | `1853:2856` |
| Token location | `figmaClient.js` → `FIGMA_TOKEN` |
| Scripts path | `.github/skills/figma/scripts/` |

---

## Error Reference

| Error | Cause | Action |
|-------|-------|--------|
| `429` | Rate limit | Wait `Retry-After` seconds, then retry |
| `403` | Token expired or wrong scope | Regenerate token |
| Node not found | Wrong NODE_ID | Confirm `-` → `:` conversion |
| Empty output | Wrong FILE_KEY or NODE_ID | Re-check URL |

All errors → apply **Design Not Found** rule above.

---

## Figma → Compose Mapping

### Layout
| Figma | Compose |
|-------|---------|
| `VERTICAL` | `Column` |
| `HORIZONTAL` | `Row` |
| `NONE` | `Box` |
| `SPACE_BETWEEN` | `Arrangement.SpaceBetween` |
| `itemSpacing: 8` | `Arrangement.spacedBy(8.dp)` |
| `padding` | `Modifier.padding(top=, end=, bottom=, start=)` |
| `layoutAlign: STRETCH` | `Modifier.fillMaxWidth()` / `fillMaxHeight()` |
| `layoutGrow: 1` | `Modifier.weight(1f)` inside Row/Column |
| `minWidth / maxWidth` | `Modifier.widthIn(min=, max=)` |

### Color
| Figma | Kotlin |
|-------|--------|
| `fills[0].hex = #FFRRGGBB` | `Color(0xFFRRGGBB)` |
| `SOLID` fill | `Modifier.background(Color(...))` |
| `GRADIENT_LINEAR` | `Brush.linearGradient(...)` |
| `opacity < 1` | `Color(...).copy(alpha = Xf)` |

### Typography
| Figma | Compose |
|-------|---------|
| `fontSize: 16` | `fontSize = 16.sp` |
| `fontWeight: 700/500/400/300` | `Bold/Medium/Normal/Light` |
| `lineHeight: 18.96` | `lineHeight = 18.96.sp` |
| `letterSpacing: 0.5` | `letterSpacing = 0.5.sp` |
| `textAlign: RIGHT` | `textAlign = TextAlign.End` |

### Shape & Effects
| Figma | Compose |
|-------|---------|
| `cornerRadius: 8` | `RoundedCornerShape(8.dp)` |
| `DROP_SHADOW` | `Modifier.shadow(elevation=Xdp, shape=...)` |
| `LAYER_BLUR radius=24` | `Modifier.blur(24.dp)` |
| `opacity: 0.5` on node | `Modifier.alpha(0.5f)` |
