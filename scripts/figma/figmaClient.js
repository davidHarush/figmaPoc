/**
 * figmaClient.js
 *
 * Shared Figma REST API client — single token, no rotation.
 *
 * Key facts (developers.figma.com/docs/rest-api):
 *  - Personal Access Token → header: X-Figma-Token: <token>
 *  - Node IDs in query strings MUST be URL-encoded ("1:2149" → "1%3A2149")
 *  - 429 response includes Retry-After (seconds) header — always respect it
 *
 * Set token via:
 *   env var:  FIGMA_TOKEN=figd_...
 *   default:  hardcoded below
 */

const { FIGMA_TOKEN } = require("./FigmaToken");
if (!FIGMA_TOKEN) {
  throw new Error("No Figma token configured. Add your token to .github/skills/figma/scripts/FigmaToken.js");
}

const BASE_URL = "https://api.figma.com";

/** Converts seconds to a human-readable string (e.g. "2 דקות ו-30 שניות") */
function formatDuration(seconds) {
  const h = Math.floor(seconds / 3600);
  const m = Math.floor((seconds % 3600) / 60);
  const s = seconds % 60;
  const parts = [];
  if (h) parts.push(`${h}h`);
  if (m) parts.push(`${m}m`);
  if (s) parts.push(`${s}s`);
  return parts.join(" ") || "less than a second";
}

const RED   = "\x1b[31m";
const RESET = "\x1b[0m";

const err = (msg) => `${RED}${msg}${RESET}`;

/**
 * Makes a GET request to the Figma REST API.
 * @param {string} path  - e.g. "/v1/files/ABC123/nodes?ids=1%3A2149"
 * @returns {Promise<object>} parsed JSON body
 */
async function figmaRequest(path) {
  const url = `${BASE_URL}${path}`;

  const res = await fetch(url, {
    headers: { "X-Figma-Token": FIGMA_TOKEN },
  });

  if (res.status === 200) {
    return res.json();
  }

  const body = await res.text();

  if (res.status === 429) {
    const retryAfterSec = Number(res.headers.get("retry-after"));
    const retryMsg = retryAfterSec
      ? `   Retry after: ${formatDuration(retryAfterSec)}\n`
      : "";
    throw new Error(err(`Figma API error 429\n${retryMsg}${body}`));
  }

  throw new Error(err(`Figma API error ${res.status}\n${body}`));
}

module.exports = { figmaRequest, FIGMA_TOKEN };
