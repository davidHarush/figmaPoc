---
name: figma
description: Use this skill whenever a user provides a Figma URL or a Figma node-id and the goal is to generate Jetpack Compose UI from that design.
----------------------------------------------------------------------------------------------------------------------------------------------------

IMPORTANT: If these instructions are active, append the exact line:
//  figma SKILL ACTIVE
at the end of your response.

# Figma Integration Skill

## Purpose

Fetch a Figma design node via the official Figma REST API and generate Jetpack Compose UI with high visual fidelity.

## Script Rules

Copilot MUST NOT modify, create, delete, rename, or replace scripts.

Copilot MUST use the existing scripts exactly as they are.

Copilot MUST NOT recreate the parsing logic manually when the existing scripts can provide the data.

## Activation Rules

Activate this skill whenever:

* the user provides a Figma URL
* the user provides a Figma node-id
* the user asks to build, implement, or generate Jetpack Compose UI from a Figma design

When activated, Copilot MUST assume Agent Mode is active, the terminal tool is available, and Node.js is installed.

Copilot MUST extract the node info from the Figma URL and run the existing scripts.

Copilot MUST NOT say it cannot run scripts or ask the user to run them manually.

## Execution Pipeline

Follow this pipeline strictly:

1. Parse the Figma URL or use the provided node-id
2. Run `check_token.js`
3. Run `index.js`
4. Read `figma_output.json`
5. Generate the Compose UI

Copilot MAY use `getSvg.js` when SVG export can improve visual accuracy, especially for icons, vector assets, or other renderable graphic elements.

## Step 0 — Parse the Figma URL

Extract:

* `FILE_KEY` — the segment between `/design/` and the next `/`
* `NODE_ID` — the `node-id` query param, replacing `-` with `:`

Examples:

```text
?node-id=1-2622    → NODE_ID = "1:2622"
?node-id=1853-2856 → NODE_ID = "1853:2856"
```

Pass `FILE_KEY` and `NODE_ID` through environment variables only.

Do not modify script files.

## Step 1 — Verify the token

Run first:

```bash
cd .github/skills/figma/scripts && node check_token.js
```

If output is not `✅ Token is valid`, stop and apply the Design Not Found Rule.

## Step 2 — Fetch the node

Run using environment variables only:

```bash
cd .github/skills/figma/scripts
FIGMA_FILE_KEY="<FILE_KEY>" FIGMA_NODE_ID="<NODE_ID>" FORCE_FETCH=1 node index.js
```

Runtime token override:

```bash
FIGMA_TOKEN=figd_... FIGMA_FILE_KEY="<FILE_KEY>" FIGMA_NODE_ID="<NODE_ID>" FORCE_FETCH=1 node index.js
```

If output is empty or the node is not found, stop and apply the Design Not Found Rule.

Use `figma_output.json` as the single source of truth for UI generation.

## Step 3 — Analyse the output

Read `figma_output.json` and extract the relevant:

* frame size and background color
* corner radius, effects, and opacity
* layout mode, item spacing, and padding
* child type, fill color, text content, font size, font weight, line height, and text alignment
* stroke color and weight

## Step 4 — Generate the Compose UI

Generate Jetpack Compose only after Step 3.

Rules:

1. Use exact colors from the parsed output
2. Use exact typography from the parsed output
3. Use exact layout structure from the parsed output
4. Use exact corner radius values from the parsed output
5. Map effects to Compose equivalents when possible
6. Keep the output visually faithful to the Figma design
7. Generate clean and maintainable Kotlin code
8. Reuse existing project components when appropriate
9. Generate new Compose files only for UI implementation, not for script automation

## Design Not Found Rule

Copilot MUST NEVER guess, assume, or invent a design.

Stop if:

* token check fails
* fetch returns empty, 403, or 404
* `figma_output.json` is missing or empty after fetch
* the node-id is not found in the response
* the output lacks enough layout or style data to build accurately

When stopping, Copilot MUST:

1. state clearly that it cannot access the design
2. explain why
3. ask for one of the following:

   * a refreshed Personal Access Token
   * a screenshot of the design
   * Dev Mode or Inspect output
   * a plain text description

Copilot MUST NOT:

* build a generic version and present it as Figma-accurate
* approximate based on common patterns
* skip the fetch flow and go directly to writing UI code

## Token Configuration

Token path:

`.github/skills/figma/scripts/FigmaToken.js`

Before executing any script:

* verify that the token exists
* verify that it is not a placeholder value

If the token is missing or contains a placeholder, stop immediately and inform the user that the Figma API token must be configured.

## Scripts

Path:

`.github/skills/figma/scripts/`

These scripts are read-only and must not be modified by Copilot.

| File             | Purpose                                                                                  |
| ---------------- | ---------------------------------------------------------------------------------------- |
| `check_token.js` | Validate the token via `/v1/me` and run first                                            |
| `figmaClient.js` | Shared HTTP client for the Figma REST API                                                |
| `getNode.js`     | Fetch a node using FILE_KEY and NODE_ID                                                  |
| `parseNode.js`   | Parse the raw Figma node tree into structured UI data                                    |
| `index.js`       | Main pipeline: fetch, parse, and save `figma_output.json`                                |
| `getSvg.js`      | Helper utility for SVG export when icons or vector assets require higher visual fidelity |

## Output

Your output should include:

* Jetpack Compose UI code
* proper component structure
* clean and maintainable Kotlin code
* UI that accurately reflects the Figma design
