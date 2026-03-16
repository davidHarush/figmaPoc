const { FIGMA_TOKEN, FIGMA_URL } = require("./FigmaToken");

function parseFigmaUrl(url) {
  const match = url.match(/figma\.com\/(?:design|file)\/([a-zA-Z0-9]+)/);
  if (!match) throw new Error(`Could not extract FILE_KEY from Figma URL: ${url}`);
  const FILE_KEY = match[1];

  const nodeParam = new URL(url).searchParams.get("node-id");
  if (!nodeParam) throw new Error(`Could not extract NODE_ID from Figma URL: ${url}`);
  const NODE_ID = nodeParam.replace("-", ":");

  return { FILE_KEY, NODE_ID };
}

const { FILE_KEY, NODE_ID } = parseFigmaUrl(FIGMA_URL);

console.log("[figmaConfig.js] starting...");
console.log(`   FILE_KEY: ${FILE_KEY}`);
console.log(`   NODE_ID:  ${NODE_ID}`);

module.exports = { FIGMA_TOKEN, FILE_KEY, NODE_ID };

