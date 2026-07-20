# green-usecase 3.1, gated — WU4 measurement (n=1)

Fourth work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commits
`5d3104f` (behavior) + `625f9ea` (refactor). Window **2026-07-16T15:27:10Z → 15:40:26Z =
796s (13m16s)**. `/refactor` applied 1 refactoring, so the unit produced **two** commits.

| Artifact | What |
|---|---|
| `backend-3.1-gated-green-usecase-wu4-timings.jsonl` | hook records, WU4 window (32) |
| `backend-3.1-gated-green-usecase-wu4-progress.txt` | per-milestone stamped progress log |

**Untracked stops: 0.** 8 agents, 8 `subagent_start`, 8 `subagent_stop` — the
`progress-hooks.md` known limitation does not bite this window.

## Where the 796s went

| Phase | Wall | Share |
|---|---|---|
| Preamble — read progress, ADR, stamp, clear log | 28.4s | 4% |
| **green-agent** | 128.0s | 16% |
| Gap: green return → coverage dispatch | 18.2s | 2% |
| **`/test-coverage usecase --focus`** | 161.7s | 20% |
| Behavior commit | 46.0s | 6% |
| **Refactor batch** — refactor 384.7s ‖ agent-review 190.4s ‖ premortem 372.3s | 395.1s | 50% |
| Tail (refactor commit) | 18.6s | 2% |

## The finding this unit produced: coverage costs more than the implementation it audits

**green-agent 128.0s vs `/test-coverage` 161.7s.** Writing the production code — three files, the
ADR's whole write path — was the *cheapest* agent in the unit and 26% cheaper than the gap-analysis
pass that followed it. Every prior unit's primary agent dominated its gate (WU3: red-agent 241.5s
vs `/test-review` 344.6s was already inverted, but only 1.4×; here the primary is 16% of the unit).

This is the first unit where the **spine is a minority of its own work unit**. green + coverage =
289.7s, 36%. The other 64% is gates, review, and commit mechanics. T2/T3's restructure targets the
smaller half.

## Batching the review passes was nearly free again — and cheaper than WU3

The three-way batch ran **947.4s of agent work in 395.1s wall — 2.40×**. Premortem (372.3s) sets
the batch wall; `/refactor` returned at 15:39:57.0, premortem at 15:40:07.4, so the two passes cost
a **marginal 10.4s**.

| Unit | Passes' marginal cost | Why |
|---|---|---|
| WU2 `design` | **338.8s** | pure serial tail — a design unit has no `/refactor` to hide behind |
| WU3 `red-usecase` | **51.3s** | `/refactor` NO ACTION, 284.9s — barely longer than premortem |
| WU4 `green-usecase` | **10.4s** | `/refactor` applied a fix + `--rerun-tasks`, 384.7s — long enough to swallow both passes |

Three points, and they line up: **the passes' marginal cost is whatever premortem overruns
`/refactor` by.** The longer `/refactor` works, the closer the passes get to free. WU3's NO-ACTION
`/refactor` was the *worst* case for hiding them, not the best. T6's lever is confirmed as the
batch.

## The stagger finding gets its controlled comparison

WU4 dispatched two fan-outs, and this time the prompt-length variable is isolated **inside one
unit, on the same machine, minutes apart**:

| Fan-out | Dispatched by | Prompt | Agents | Stagger |
|---|---|---|---|---|
| Refactor batch | orchestrator | **long** (ADR + known-findings context inlined, ~45 lines each) | 3 | **11.4s** |
| `/refactor` detectors | refactor-agent | **short** (path + cluster name) | 3 | **6.3s** |

1.8× spread, same dispatch machinery, same message. Together with WU2 (13.5s, full design inlined)
and WU3 (5.2–7.5s, short prompts), the picture across four fan-outs is consistent: **the serial
prefix is the cost of emitting each prompt**, and it scales with prompt length, not agent count.

Note this cuts against the orchestrator: WU4's batch prompts were long *because I inlined the ADR
and the accumulated known-findings list into all three*. Writing the artifact to a file and passing
a path is still the untested fix. Now four points; still owed a test against T3.

## Verdicts (both non-gating; commits stand)

- **agent-review: CONCERNS (2).** (1) **`addToToDo` mutates a list whose mutability nothing
  guarantees** — `H2BoardStorage` fills `Column.tasks` via `Collectors.toList()`, whose mutability
  the JDK explicitly leaves unspecified; the fake can never expose this because `Column.empty()`
  hands back an `ArrayList`. A stock "modernize to `Stream.toList()`" refactor would 500 every
  create with all 10 tests green. (2) **`TO_DO` is stated twice and can silently diverge** —
  `board.addToToDo(...)` encodes the column, then `taskStorage.save(task, ColumnType.TO_DO)`
  re-asserts it; nothing ties them together, and `FakeTaskStorage.save` records neither argument,
  so no test could catch a divergence.
- **premortem: CONCERNS (1 credible).** **The test-only cleanup endpoint became a data-loss
  weapon.** `CleanupController` carries `@Profile("!prod")` — fail-open, live in prod-copy,
  staging, or no-profile — over a `TRUNCATE TABLE tasks`, and `AbstractBackendTest.@BeforeEach`
  calls it unconditionally against whatever `BACKEND_URL` names. **This unit is the trace**: before
  `taskStorage.save`, no code path ever inserted a row, so the truncate was a no-op everywhere,
  forever. `/qa-run` deliberately points at prod-copy; the two are one env var apart. No named
  deferral owns it (S8 owns the datasource URL, S1–S7 own write-path semantics). One-line fix:
  `@Profile("test")` — fail-closed.

## Premortem falsified a design-commit claim

The design commit asserted `hibernate.jdbc.time_zone` is load-bearing at the first write ("cheap to
pin now — setting it later shifts existing rows"). Premortem read Hibernate 6.4.4 from the Gradle
cache and showed the premise is **false for this stack**:
`ConfigurationHelper.getPreferredSqlTypeCodeForInstant` defaults to `SqlTypes.TIMESTAMP_UTC`, whose
binder uses an explicit `UTC_CALENDAR` — independent of both the JVM default zone and
`hibernate.jdbc.time_zone`, which governs the `Timestamp`/`LocalDateTime` paths, not `Instant`. The
ADR's config line is harmless but not load-bearing. Whoever runs `green-adapter storage` should
know that before treating it as urgent.

## Two java-spring template defects found by coverage-agent

Both would silently corrupt any focus-mode coverage run:

1. **`.claude/tech/java-spring/templates/testing/coverage-commands.md` — awk column offsets off by
   one.** JaCoCo CSV is `BRANCH_MISSED(6), BRANCH_COVERED(7), LINE_MISSED(8), LINE_COVERED(9)`; the
   commands read `$7/$8` as branches and `$9/$10` as lines. As written it reports
   `CreateTaskUseCase — lines: 0/7` on a passing test.
2. **The focus filter returns nothing.** `git diff HEAD --name-only -- 'backend/*/src/main/'`
   matches zero paths (`:(glob)backend/*/src/main/**` works), and `git diff HEAD` can't see
   untracked files — `ColumnNotFoundException.java` carried 2 of the 4 uncovered lines.

Also: the gradle wrapper at the repo root is **not executable**. `./gradlew` dies with "Permission
denied", and `/refactor` reported the exit code was masked by a `| tail` pipe while stale green
result XMLs from the gate's run made it look like a pass. `sh gradlew` works. This is adjacent to
Task 18 Step 0 but distinct from it.

## Carry-over

- **Nothing was fixed.** Measurement run; the passes are non-gating. A real run would owe a decision
  on the `@Profile("!prod")` truncate endpoint before any deployed environment sees a write.
- The S3 orphan is now **three-for-three**. Premortem's line on it is sharper than WU2's and WU3's:
  this unit is where its absence stops being theoretical — `save` is now the only unguarded
  reachable write, and 3.1 has no test-writing window left (the two inserted coverage steps are
  scoped to `ColumnNotFoundException`).
- **Coverage guarded the unreachable branch while the reachable one stayed unguarded.** Both passes
  independently reached this. `CreateTaskUseCase` reports 100% line coverage *because* the test
  executes `save()` without asserting on it — the gap is structurally invisible to the tool.
- No backend was started. WU4 is **not** contaminated by the Task 18 Step 0 breakage — a clean
  number, like WU2 and WU3.
- The per-milestone stamp addendum held with no misses this unit.
