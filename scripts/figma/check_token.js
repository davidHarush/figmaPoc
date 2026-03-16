#!/usr/bin/env node
/**
 * check_token.js
 *
 * Validates the configured Figma token using GET /v1/me
 * This is a FREE endpoint — costs 0 of your monthly quota.
 *
 * Usage:
 *   node check_token.js
 *   FIGMA_TOKEN=figd_... node check_token.js
 */

const { FIGMA_TOKEN } = require("./FigmaToken");

(async () => {
  console.log("[check_token.js] starting...\n");
  const masked = FIGMA_TOKEN.slice(0, 12) + "..." + FIGMA_TOKEN.slice(-4);
  console.log(`🔍 Checking Figma token: ${masked}\n`);

  try {
    const res = await fetch("https://api.figma.com/v1/me", {
      headers: { "X-Figma-Token": FIGMA_TOKEN }
    });

    if (res.status === 200) {
      const data = await res.json();
      console.log(`✅ Token is valid`);
      console.log(`   User:  ${data.handle || data.email || "unknown"}`);
      console.log(`   Email: ${data.email || "n/a"}`);
    } else if (res.status === 403) {
      console.log(`❌ 403 Forbidden — token is invalid or expired`);
      console.log(`   → Generate a new token at: figma.com → Settings → Security → Personal access tokens`);
    } else if (res.status === 429) {
      const retryAfter = res.headers.get("retry-after") || "unknown";
      console.log(`⚠️  429 Rate Limited (retry after ${retryAfter}s)`);
    } else {
      const body = await res.text();
      console.log(`⚠️  Unexpected response ${res.status}: ${body.slice(0, 100)}`);
    }
  } catch (err) {
    console.log(`💥 Network error: ${err.message}`);
  }
})();
