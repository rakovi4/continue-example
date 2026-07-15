# Task 16: Scenario Dependency Tree + Multi-Stream Progress -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Per-story dependencies.md
- [ ] refactor (dependencies.md format + template; written at spec time, updated on scenario adds)

### Step 2: Stream-scoped progress files
- [ ] refactor (progress-{stream}.md format; progress.md becomes the stream index)
- [ ] refactor (update single-source-of-truth wording in rules/workflow.md)

### Step 3: Stream-aware /continue

> BLOCKED by Task 9 — dispatch is being rewritten as Workflow scripts; implement
> the stream claim against the new dispatch, not the current prose SKILL.

- [ ] refactor (stream claim + next-unit-within-stream selection)
- [ ] refactor (unblocking-first ordering from the dependency tree)

### Step 4: Cross-story feeding
- [ ] refactor (session-start recommendation: which story/stream is unblocked now)
