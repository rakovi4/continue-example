# Task 16: Scenario Dependency Tree + Multi-Stream Progress

Type: refactoring

Framework speed-up initiative **T9** — see
[framework-speedup/README.md](../../framework-speedup/README.md) and
[checklist items N1, N2, N8](../../framework-speedup/checklist.md).

## Problem

Giant stories (~100 test cases) take weeks when worked as a single serial
stream, and agents give no help parallelizing across stories/tasks — keeping
3–4 sessions fed is manual guesswork. The team already evolved a working
practice: separate progress files per stream within one story, plus a
dependency tree answering "which test scenarios block another stream of work".
It lives only in team copies and heads; the framework should formalize it.

## Solution

### Step 1: Per-story `dependencies.md`

A per-story file declaring which scenarios block which (and which are
independent). Written at spec time, updated when scenarios are added (e.g. by
coverage). This is the source both stream-splitting and ordering read from.

### Step 2: Stream-scoped progress files

Split story progress into per-stream files (`progress-{stream}.md`).
`progress.md` remains the **single source of truth as an index**: which streams
exist, which is claimed by which session, pointer to each stream file — this
reconciles multi-stream with the "single source of truth for state" rule in
`.claude/rules/workflow.md`, which must be updated to say exactly that.

### Step 3: Stream-aware `/continue`

`/continue` claims a stream and picks the next work unit **within it**;
ordering surfaces unblocking-first (scenarios that unblock other streams come
before same-stream convenience order).

### Step 4: Cross-story feeding (N2)

The same dependency tree answers "what do I feed session N with" across
stories: which story/stream is unblocked right now. Expose that as the
session-start recommendation in `/continue`.

## Key Files

- `.claude/rules/workflow.md` (single-source-of-truth wording → index model)
- `.claude/guidelines/workflow-detail.md` (progress mechanics, resuming)
- `.claude/skills/continue/SKILL.md` (stream claim + next-unit selection)
- `.claude/templates/workflow/` (dependencies.md + stream progress formats)

## Dependencies

Independent; coordinate with Task 9 (the Workflow scripts must read
stream-scoped progress files).
