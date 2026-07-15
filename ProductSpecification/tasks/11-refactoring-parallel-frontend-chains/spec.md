# Task 11: Parallel Frontend Red-Green Chains

Type: refactoring

Framework speed-up initiative **T4** — see
[framework-speedup/README.md](../../framework-speedup/README.md) (D6, D7) and
[checklist items 1.9a, 1.9b](../../framework-speedup/checklist.md).

## Problem

The frontend flow runs the application-layer red-green cycle and the client
red-green cycle strictly in sequence, with a human review phase between them.
Same shape as the backend adapter problem (Task 10): independent chains
serialized by convention plus a human handoff that exists only because the
chains are serialized.

## Solution

In the Workflow orchestration (Task 9): run the application red-green chain in
parallel with the client red-green chain.

- Remove the human review phase between the red-green cycles (D6). The work
  unit's pre-commit review passes still run once at the end.
- Commits follow the single-committer discipline (D7); chains touching the
  same file escalate to worktree isolation.
- Update the frontend scenario sequence in `workflow-detail.md` accordingly.
- Verify against `frontend-rules.md` conventions — read it before changing the
  frontend sequence.

Expected impact (hypothesis until Task 8 numbers): high, on every frontend
scenario.

## Key Files

- `.claude/guidelines/workflow-detail.md` (frontend scenario sequence)
- `.claude/guidelines/frontend-rules.md`
- `.claude/skills/continue/SKILL.md`
- `.claude/workflows/` (script from Task 9)

## Dependencies

After Task 9 (Workflow orchestration + D7 commit discipline).
