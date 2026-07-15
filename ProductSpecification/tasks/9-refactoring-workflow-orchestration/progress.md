# Task 9: Workflow-Based Deterministic Orchestration -- Progress

Type: refactoring

## Spec
- [x] spec

## Fix

### Step 1: Workflow scripts for dispatch sequences
- [ ] refactor (author Workflow scripts for the work-unit dispatch sequences)
- [ ] refactor (wire /continue to invoke scripts, model judgment via args)

### Step 2: Single-committer discipline (D7)
- [ ] refactor (agents return file lists, never commit; script commits serially by pathspec)
- [ ] refactor (git status verification: unclaimed modified file stops the run)
- [ ] refactor (worktree escalation for same-file overlap between concurrent units)

### Step 3: Update the workflow rules
- [ ] refactor (update continue/SKILL.md and workflow-detail.md to the script-based dispatch)
