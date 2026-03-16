/**
 * getNode.js
 *
 * Fetches a single Figma node by FILE_KEY + NODE_ID.
 *
 * Official endpoint: GET /v1/files/:key/nodes?ids=:ids
 */

const { figmaRequest } = require("./figmaClient");

const FILE_KEY = process.env.FIGMA_FILE_KEY;
const NODE_ID  = process.env.FIGMA_NODE_ID;

if (!FILE_KEY) {
  throw new Error("FIGMA_FILE_KEY environment variable is required");
}

if (!NODE_ID) {
  throw new Error("FIGMA_NODE_ID environment variable is required");
}
// ─────────────────────────────────────────────────────────────────────────────

async function getNode() {
  // Node IDs MUST be URL-encoded: colon → %3A
  const encodedId = encodeURIComponent(NODE_ID);
  const data = await figmaRequest(`/v1/files/${FILE_KEY}/nodes?ids=${encodedId}`);

  const nodeWrapper = data.nodes[NODE_ID];
  if (!nodeWrapper) {
    throw new Error(
      `Node "${NODE_ID}" not found in response. ` +
      `Available keys: ${Object.keys(data.nodes).join(", ")}`
    );
  }

  return {
    document:   nodeWrapper.document,
    components: nodeWrapper.components || {},
    styles:     nodeWrapper.styles     || {},
  };
}

module.exports = { getNode, FILE_KEY, NODE_ID };
