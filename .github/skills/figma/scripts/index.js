#!/usr/bin/env node

const fs = require("fs");
const path = require("path");

const { getNode } = require("./getNode");
const { parseNode } = require("./parseNode");

const OUT_FILE = path.join(__dirname, "figma_output.json");
const CACHE_MAX_MS = 7 * 24 * 60 * 60 * 1000; // 7 days


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


(async () => {

  const forceFetch = process.env.FORCE_FETCH === "1";

  if (!forceFetch) {
    const cached = loadCache();

    if (cached) {
      console.log("FIGMA_FETCH_COMPLETE");
      return;
    }
  }

  const { document, components, styles } = await getNode();

  const parsed = parseNode(document);

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

  fs.writeFileSync(
    OUT_FILE,
    JSON.stringify(output, null, 2),
    "utf-8"
  );

  console.log("FIGMA_FETCH_COMPLETE");

})().catch((err) => {

  console.error("FIGMA_FETCH_ERROR:", err.message);

  process.exit(1);

});