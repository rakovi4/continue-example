# Task 3: Composite Tech Profiles -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Update technology-loading.md for composite profiles
- [x] update technology-loading.md — new composite format, resolution protocol per concern
- [x] update technology-references.md — document new structure

### Step 2: Update technology.md declaration format
- [x] rewrite ProductSpecification/technology.md — composite tech-profile with 4 concern keys
- [x] update Conventions table — split by concern (backend, frontend, css, browser-testing)

### Step 3: Extract react-ts profile
- [x] create .claude/tech/react-ts/coding.md — humble object, feature structure, naming, shared UI
- [x] create .claude/tech/react-ts/tdd.md — Vitest, MSW, .skip marker, base URL config
- [x] create .claude/tech/react-ts/infrastructure.md — Vite commands, VITE_API_URL, npm commands
- [x] create .claude/tech/react-ts/templates/ — move logic-test.md, api-test.md, implementation.md

### Step 4: Extract tailwind profile
- [x] create .claude/tech/tailwind/coding.md — utility extraction rules, @apply, theme.css, icon library

### Step 5: Extract selenium profile
- [x] create .claude/tech/selenium/coding.md — 2-tier DSL, data-testid, locator rules, mass failure diagnosis
- [x] create .claude/tech/selenium/tdd.md — Selenium Statements assertions, WireMock stubs
- [x] create .claude/tech/selenium/templates/ — move selenium-test.md, align-design-checklist.md, design-review-patterns.md

### Step 6: Trim java-spring profile
- [x] remove frontend/CSS/Selenium content from java-spring/frontend.md (delete file)
- [x] remove Selenium assertions from java-spring/tdd.md
- [x] remove frontend commands from java-spring/infrastructure.md
- [x] remove java-spring/templates/frontend/ (moved to react-ts + selenium)

### Step 7: Update agent/skill profile resolution
- [x] update agents that read technology.md — resolve correct concern per layer
- [x] update skills that read technology.md — resolve correct concern per layer
- [x] update frontend-rules.md references (if any point to tech binding paths)
- [x] delete java-spring/frontend.md (redundant — content extracted in Steps 3-5)
- [x] update documentation templates (prompt-update-classification.md, prompt-scan-checklist.md)

### Step 8: Verify nothing lost
- [x] diff content coverage — every line from old java-spring exists in exactly one new profile
- [x] grep for stale tech-profile references across .claude/
- [x] verify all template paths still resolve
