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

/** Sleep helper */
const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

/**
 * Makes a GET request to the Figma REST API.
 * On 429: waits for Retry-After and retries up to maxRetries times.
 * @param {string} path  - e.g. "/v1/files/ABC123/nodes?ids=1%3A2149"
 * @param {object} opts  - optional: { maxRetries: 3 }
 * @returns {Promise<object>} parsed JSON body
 */
async function figmaRequest(path, { maxRetries = 3 } = {}) {
  const url = `${BASE_URL}${path}`;
  let attempts = 0;

  while (true) {
    const res = await fetch(url, {
      headers: { "X-Figma-Token": FIGMA_TOKEN },
    });

    if (res.status === 200) {
      return res.json();
    }

    if (res.status === 429) {
      const retryAfterSec = Number(res.headers.get("retry-after")) || 60;

      if (attempts >= maxRetries) {
        throw new Error(
          `❌ RATE LIMITED — Figma returned 429.\n\n` +
          `TO FIX:\n` +
          `  1. figma.com → avatar → Settings → Security → Personal access tokens\n` +
          `  2. Generate a NEW token (scope: file_content:read)\n` +
          `  3. Update FIGMA_TOKEN in .github/skills/figma/scripts/FigmaToken.js`
        );
      }

      console.warn(
        `[figmaClient] Rate limited (429). Retrying in ${retryAfterSec}s... (attempt ${attempts + 1}/${maxRetries})`
      );
      await sleep(retryAfterSec * 1000);
      attempts++;
      continue;
    }

    if (res.status === 403) {
      throw new Error(
        `Figma API 403 Forbidden. ` +
        `Token may be expired or missing file_content:read scope. ` +
        `Update the token in .github/skills/figma/scripts/FigmaToken.js`
      );
    }

    const body = await res.text();
    throw new Error(`Figma API error ${res.status}: ${body.slice(0, 300)}`);
  }
}

module.exports = { figmaRequest, FIGMA_TOKEN };
