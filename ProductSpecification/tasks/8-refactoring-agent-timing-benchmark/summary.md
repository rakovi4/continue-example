# Task 8 — Journey Summary

## refactor hooks Step 1 (2026-07-15)

**Quirk:** On Agent-tool `return` records, the hook payload's top-level `agent_id` is the *dispatching* agent's id, not the dispatched subagent's — the dispatched subagent's id lives at `tool_response.agentId`, which the script does not extract.
**Where:** `.claude/hooks/agent-timing.sh` (jq fallback chain `$in.agent_id // $in.agentId`), observed live in `infrastructure/timings/agent-timing-<session>.jsonl`.
**Implication:** Step 2 must not join the tool-call family to the subagent family on `agent_id` as recorded — either extract `tool_response.agentId` for return events first, or join via `agent_type` + time and assert type match on both sides.

**Quirk:** A `SendMessage` resume of a completed subagent fires a fresh `SubagentStart` — one `agent_id` can emit multiple start/stop cycles, including a stop timestamped *before* the next start; mid-session hook activation also leaves orphan stops with no start.
**Where:** SubagentStart/SubagentStop hook events; observed live for the resumed premortem agent.
**Implication:** Aggregation must pair each stop with the latest unmatched start of that id and fail loudly on orphans — naive first-start/last-stop pairing yields inflated or negative durations.

## refactor hooks Step 1, refactor batch (2026-07-15)

**Mistake:** Edited `.claude/hooks/agent-timing.sh` in the main session while the concurrently dispatched `/refactor` agent was mutating the same file — the main agent's Edit was rejected ("file modified"), and the merged result had to subsume the refactor pass's change into a behavior commit, leaving no separate refactor commit.
**Why wrong:** The refactor-batch overlap design assumes the review passes read the immutable commit and only `/refactor` writes the tree; a second concurrent tree writer breaks the no-race assumption.
**Correct location/approach:** While the `/refactor` batch is in flight, the main agent must not edit files the work unit touched — queue amendments until the refactor agent returns, then rebase them on its version.

## refactor (no-review replay 1.1→2.3) (2026-07-15)

**Decision:** The D8 replay is executed inline as per-scenario reconstruction commits (template-strip, then 1.1 / 2.1 / 2.2 / 2.3, each restoring main's real test files plus minimally trimmed production code), not as agent-driven red/green re-runs.
**Why:** The checkbox is a `refactor (...)` dispatch (apply-inline); D8 requires only an identical starting commit, and main's reviewed test files are more faithful than regenerated ones.
**Where applied:** branch `benchmark/pre-3.1` — template `494beaa` → 1.1 `dd4b036` → 2.1 `49aec8f` → 2.2 `0cfe41b` → 2.3 `f1e361a` (hashes are post-rewrite; see verification-trap entry).

**Decision:** "Template commit" = current HEAD minus all story-1 implementation (code, tests, story ADRs, frontend feature code, tasks migration), committed as the branch's first commit — not the repo's initial commit.
**Why:** Implementation history is squashed into the initial commit, which also predates the h2→storage module rename; the fixture must carry current framework tooling for the old-implementation ceiling run.
**Where applied:** first commit of `benchmark/pre-3.1` (`494beaa`); `benchmark/pre-ui-1.1` will fork from this branch.

## refactor (no-review replay 1.1→2.3) — ADR scoping (2026-07-15)

**Decision:** 3.1-era ADRs (task-id-strategy, board-aggregate-mutation) are stripped from the fixture; board-domain-model is restored with scenario 1.1 and validation-pattern with 2.1.
**Why:** Leaving 3.1's ADRs on the fixture would hand the benchmark run its own design answers, shrinking the measured design step.
**Where applied:** `ProductSpecification/Stories/01-create-task/decisions/` on `benchmark/pre-3.1`.

## refactor (no-review replay 1.1→2.3) — environment (2026-07-15)

**Quirk:** `gradlew` is tracked without the exec bit (mode 100644) and `infrastructure/scripts/*.sh` hardcode Windows specifics (JAVA_HOME=/c/…, taskkill), so `./gradlew` and run-backend.sh fail on this Linux host.
**Where:** `gradlew`, `infrastructure/scripts/`
**Implication:** Invoke `bash gradlew …`; start the backend directly with `BACKEND_PORT` from `infrastructure/.env` and kill only the PID you started; `.env` is gitignored — copy it into any worktree.

## refactor (no-review replay 1.1→2.3) — verification trap (2026-07-15)

**Mistake:** Verified the template-state build with `./gradlew … | tail`, which masked the failure — gradlew never executed (Permission denied) while the pipeline exited 0.
**Why wrong:** A pipeline's exit status is the last command's, so the "compile check" for template commit `eff2469` never actually ran.
**Correct location/approach:** Capture gradle's own exit code (`bash gradlew … > log 2>&1; echo EXIT=$?`). The re-check indeed failed: the template had kept `GetEmptyBoardUseCaseTest` while stripping its base class `ApplicationTest`. Fixed by rewriting the branch (amend template, re-add the file in the 1.1 commit, cherry-pick the rest — final tree byte-identical to the verified one); template `494beaa` now compiles clean.

## discussion (2026-07-16)

**Decision:** The fixture will NOT be seeded to force coverage gaps; coverage's detection cost (285.8s, constant, already measured) is separated from its fan-out cost (N gaps → N parallel reds), and only the latter needs gaps — it gets its own measurement when Task 12 runs.
**Why:** Gaps are emergent from what the green agent writes *during* the run, not a property of the pre-3.1 start state, so no starting commit can force them; seeding would also tune the fixture toward the result we want to measure.
**Where applied:** checklist N14, Task 8 spec "Known fixture limitation" — supersedes the in-conversation agreement to seed both fixtures.

**Decision:** Ceiling runs execute alone on an idle host — never two benchmarks concurrently, and Task 9 in a parallel repo is not free either.
**Why:** The old arm runs now and the new arm weeks later, so contention noise does not cancel across the comparison — it becomes permanent bias in the old arm, in the direction that flatters the ~66% hypothesis.
**Where applied:** Task 8 Step 4 ceiling runs — backend, then frontend, serially.

## refactor hooks Step 1 (2026-07-16)

**Quirk:** `infrastructure/timings/` is no longer gitignored, and the hooks append to the session log continuously — the tree is dirty for the whole session, so a rebase needs `git stash push -- infrastructure/timings/` first.
**Where:** `.gitignore`, `.claude/hooks/agent-timing.sh`, `.claude/settings.json`.
**Implication:** Expect a dirty tree in any session; no `SessionEnd` hook is registered, so nothing archives the log when it is final — long benchmark sessions make the churn worse, not better.
