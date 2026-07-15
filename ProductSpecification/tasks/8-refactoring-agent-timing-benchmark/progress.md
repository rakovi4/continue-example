# Task 8: Agent Timing Instrumentation + Ceiling Benchmark -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Hook-emitted agent timings
- [ ] refactor (PreToolUse/PostToolUse hooks on Agent tool + JSONL timing log)

### Step 2: Time-budget aggregation
- [ ] refactor (aggregation script producing the per-step time-budget table)

### Step 3: Synthetic benchmark fixtures (D8)
- [ ] refactor (no-review replay of story 1 scenarios 1.1→2.3 → branch benchmark/pre-3.1)
- [ ] refactor (continue replay through story 1 backend → branch benchmark/pre-ui-1.1)

### Step 4: Ceiling baseline runs (D4)
- [ ] refactor (backend ceiling run: scenario 3.1 from benchmark/pre-3.1, old implementation)
- [ ] refactor (frontend ceiling run: story 1 UI scenario 1 from benchmark/pre-ui-1.1, old implementation)
- [ ] refactor (record baseline time-budget table in framework-speedup README)
