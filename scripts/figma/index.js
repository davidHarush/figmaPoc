#!/usr/bin/env node

const fs = require("fs");
const path = require("path");

const { getNode } = require("./getNode");
const { parseNode } = require("./parseNode");

const GREEN = "\x1b[32m";
const RESET = "\x1b[0m";
const ok = (msg) => `${GREEN}${msg}${RESET}`;
const OUT_FILE = path.join(__dirname, "figma_output.json");
const CACHE_MAX_MS = 0; // always fetch fresh


function loadCache() {
  if (!fs.existsSync(OUT_FILE)) return null;

  try {
    const raw = fs.readFileSync(OUT_FILE, "utf-8");
    const data = JSON.parse(raw);

    if (!data._meta?.fetchedAt) return null;

    const age = Date.now() - new Date(data._meta.fetchedAt).getTime();

    if (age < CACHE_MAX_MS) {
      return data;
    }

    return null;
  } catch {
    return null;
  }
}


function countNodes(node) {
  return 1 + (node.children || []).reduce((sum, c) => sum + countNodes(c), 0);
}

(async () => {
  console.log("[index.js] starting...\n");

  const forceFetch = process.env.FORCE_FETCH === "1";

  if (!forceFetch) {
    const cached = loadCache();

    if (cached) {
      console.log(ok(`✅ Done (from cache — fetched at ${cached._meta.fetchedAt})`));
      console.log(ok(`   📄 UI output → ${OUT_FILE}`));
      console.log("FIGMA_FETCH_COMPLETE");
      return;
    }
  }

  console.log("🔄 Fetching node from Figma...");
  const { document, components, styles } = await getNode();
  console.log(`✅ Node fetched: "${document.name}" (${document.id})`);

  console.log("🔍 Parsing node tree...");
  const parsed = parseNode(document);
  const totalChildren = countNodes(parsed);
  console.log(`✅ Parsed ${totalChildren} nodes`);

  const output = {
    _meta: {
      fetchedAt: new Date().toISOString(),
      nodeId: parsed.id,
      nodeName: parsed.name
    },
    node: parsed,
    components,
    styles
  };

  fs.writeFileSync(OUT_FILE, JSON.stringify(output, null, 2), "utf-8");
  console.log(`💾 Saved to ${OUT_FILE}`);
  console.log(ok(`────────────────────────────────────────────────────────────────────────`));
  console.log(ok(`✅  Done! The Figma UI tree is ready.`));
  console.log(ok(` Open figma_output.json to review the full UI structure and styles.`));
  console.log(ok(`────────────────────────────────────────────────────────────────────────`));
  console.log("FIGMA_FETCH_COMPLETE");


})().catch((err) => {
  console.error("FIGMA_FETCH_ERROR:", err.message);
  process.exit(1);

});