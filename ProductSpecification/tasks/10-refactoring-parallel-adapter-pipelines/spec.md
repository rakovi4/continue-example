# Task 10: Parallel Adapter Red+Green Pipelines

Type: refactoring

Framework speed-up initiative **T3** — see
[framework-speedup/README.md](../../framework-speedup/README.md) (D6, D7) and
[checklist items 1.5a, 1.5b](../../framework-speedup/checklist.md).

## Problem

After `adapters-discovery` finds N adapters (typically rest + storage), their
red-adapter → green-adapter cycles run strictly serially, with a human review
stop between each pair. The pairs are independent by construction — each
implements a different port against the same already-green usecase — so the
serialization and the inter-pair human handoffs are pure waiting.

## Solution

In the Workflow orchestration (Task 9): `pipeline(adapters, red, green)` — each
discovered adapter runs its red+green chain concurrently with the others
(e.g. red-rest+green-rest ∥ red-storage+green-storage).

- Remove the human review step between adapter pairs (D6) — required for the
  pipeline to be a pipeline at all. The work unit's normal pre-commit review
  passes still run once at the end.
- Commits follow the single-committer discipline (D7): the script lands one
  red and one green commit per adapter, serially, staged by pathspec from each
  agent's returned file list.
- Adapter pairs touching the same file (rare; e.g. shared wiring in
  `application`) escalate to worktree isolation per D7.
- Update the backend scenario sequence in `workflow-detail.md` accordingly.

Expected impact (hypothesis until Task 8 numbers): ~15–20% per backend
scenario.

## Key Files

- `.claude/guidelines/workflow-detail.md` (backend scenario sequence)
- `.claude/skills/continue/SKILL.md`
- `.claude/workflows/` (script from Task 9)

## Dependencies

After Task 9 (Workflow orchestration + D7 commit discipline).
