# Task 7: Prompt Framework Bottlenecks -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Split test-review-patterns by layer
- [x] refactor (split java-spring test-review-patterns.md into per-layer files)
- [x] refactor (split remaining 6 backend profiles)
- [x] refactor (update test-review-agent.md to load layer-specific file)

### Step 2: Pass tech profile as parameter
- [S] refactor (update continue/SKILL.md to pass resolved profile) — adds more complexity than it saves; reading a 95-line file 3x is cheaper than 74 lines of protocol docs
- [S] refactor (update tech-aware skills to accept profile parameter)

### Step 3: Merge smell-routing-table into scan-checklist
- [x] refactor (move routing table into scan-checklist.md, delete smell-routing-table.md)
- [x] refactor (update refactor-agent.md references)
