# Task 13: Review-Pass + Gate-Agent Parallelism -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Confirm with Task 8 data
- [ ] refactor (identify serial tails from the per-step time-budget table)

### Step 2: Enforce fan-out in the Workflow scripts
- [ ] refactor (detection clusters M/D/T and A/P/Se/S via parallel())
- [ ] refactor (review passes concurrent with the step's quality gates)

### Step 3: Start review passes earlier
- [ ] refactor (diff-snapshot input so passes start before the behavior commit)

### Step 4: Speed up the agents themselves
- [ ] refactor (trim per-agent loaded context to the relevant layer/cluster)
- [ ] refactor (model/effort tier per detection cluster, guided by the data)
