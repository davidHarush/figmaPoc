/**
 * getSvg.js
 *
 * Exports a node as an SVG via the Figma images API and optionally
 * downloads the SVG content to a local file.
 *
 * Official endpoint: GET /v1/images/:key?ids=:ids&format=svg
 * CRITICAL: Node IDs MUST be URL-encoded in the query string.
 *
 * Usage (standalone):
 *   node getSvg.js                              # prints SVG URL for default NODE_ID
 *   FIGMA_NODE_ID=1:1234 node getSvg.js         # custom node
 *   DOWNLOAD=1 FIGMA_NODE_ID=1:1234 node getSvg.js  # download SVG content to file
 */

const fs   = require("fs");
const path = require("path");
const { figmaRequest } = require("./figmaClient");
const { FILE_KEY, NODE_ID } = require("./getNode");

/**
 * Returns the CDN URL of the rendered SVG for the given node.
 * @param {string} nodeId - Figma node ID (e.g. "1:1234")
 * @returns {Promise<string>} CDN URL
 */
async function getSvgUrl(nodeId = NODE_ID) {
  const encodedId = encodeURIComponent(nodeId);
  const data = await figmaRequest(
    `/v1/images/${FILE_KEY}?ids=${encodedId}&format=svg&svg_include_id=true`
  );

  if (data.err) throw new Error(`Figma images API error: ${data.err}`);

  const svgUrl = data.images[nodeId];
  if (!svgUrl) {
    throw new Error(
      `No SVG URL returned for node "${nodeId}". ` +
      `The node may be invisible or have no renderable content.`
    );
  }
  return svgUrl;
}

/**
 * Downloads the SVG content from the CDN URL and saves it to a local file.
 * @param {string} nodeId     - Figma node ID
 * @param {string} outputPath - local file path to write (default: <nodeId>.svg in scripts/)
 * @returns {Promise<string>} the outputPath written
 */
async function downloadSvg(nodeId = NODE_ID, outputPath) {
  const url = await getSvgUrl(nodeId);
  const res = await fetch(url);
  if (!res.ok) throw new Error(`Failed to download SVG: HTTP ${res.status}`);
  const svg = await res.text();

  const safeId  = nodeId.replace(/:/g, "_");
  const outFile = outputPath || path.join(__dirname, `${safeId}.svg`);
  fs.writeFileSync(outFile, svg, "utf-8");
  return outFile;
}

module.exports = { getSvgUrl, downloadSvg };

// ── Standalone runner ────────────────────────────────────────────────────────
if (require.main === module) {
  const nodeId   = NODE_ID;
  const download = process.env.DOWNLOAD === "1";

  (async () => {
    console.log("[getSvg.js] starting...\n");
    if (download) {
      const file = await downloadSvg(nodeId);
      console.log(`✅ SVG saved to: ${file}`);
    } else {
      const url = await getSvgUrl(nodeId);
      console.log(`🖼️  SVG URL: ${url}`);
    }
  })().catch((err) => {
    console.error("❌ Error:", err.message);
    process.exit(1);
  });
}
