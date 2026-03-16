/**
 * getNode.js
 *
 * Fetches a single Figma node by FILE_KEY + NODE_ID.
 *
 * Official endpoint: GET /v1/files/:key/nodes?ids=:ids
 */

const { figmaRequest } = require("./figmaClient");
const { FILE_KEY, NODE_ID } = require("./figmaConfig");
// ─────────────────────────────────────────────────────────────────────────────

async function getNode() {
  const encodedId = encodeURIComponent(NODE_ID);
  console.log(`   File: ${FILE_KEY}  Node: ${NODE_ID}`);
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
