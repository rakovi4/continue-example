# Task 14: Auto-Apply Review-Agent Concern Fixes

Type: refactoring

Framework speed-up initiative **T7** — see
[framework-speedup/README.md](../../framework-speedup/README.md) (D3) and
[checklist items 2, N6](../../framework-speedup/checklist.md).

## Problem

Review-pass concerns (agent-review + premortem) are displayed at the end of a
work unit as informational notes only. Fixing them requires a manual ask after
each `/continue` — one extra human round-trip per work unit. The team has
already patched their framework copies to auto-accept the fixes and review them
post-factum; the upstream framework should reflect the de-facto practice
instead of fighting it (D3).

## Solution

Blanket auto-apply (D3): review-pass concern fixes are applied and committed
automatically, surfaced for post-factum review.

- Review passes emit actionable fix lists (not prose observations).
- An apply step lands the fixes as a **separate commit** after the work unit's
  final commit, so they stay auditable and revertable as a unit.
- The work-unit report lists what was auto-applied, keeping the post-factum
  review cheap.
- Review passes remain non-gating; a concern the applier cannot mechanically
  resolve is reported, never guessed at.

This also serves as the currently available mitigation for review-thoroughness
drift (checklist N6): concerns get applied instead of ignored.

## Key Files

- `.claude/skills/continue/SKILL.md` (Pre-Commit Review Passes → apply step)
- `.claude/agents/agent-review-agent.md`, `.claude/agents/premortem-agent.md`
  (output format: actionable fix list)
- `.claude/guidelines/workflow-detail.md` (work-unit commit sequence)

## Dependencies

None — independent of the Workflow conversion; coordinate the commit point
with Task 9 if it has landed.
