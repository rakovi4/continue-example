# Task 12: Coverage-Red Fan-Out + Cross-Unit Overlap -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Parallel coverage reds
- [ ] refactor (N gaps → N parallel red agents + 1 green after, in the Workflow script)

### Step 2: Cross-unit overlap
- [ ] refactor (coverage reds start during the original green's refactor+review batch)
- [ ] refactor (update atomic-work-unit wording in rules/workflow.md for the sanctioned overlap)

### Step 3: Sequence + rules update
- [ ] refactor (update coverage sequence in workflow-detail.md and tdd-rules.md)
- [ ] refactor (verify D7 refactor-vs-coverage commit attribution on a real scenario)
