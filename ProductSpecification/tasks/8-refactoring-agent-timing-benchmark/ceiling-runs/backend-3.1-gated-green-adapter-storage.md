# green-adapter storage 3.1, gated — WU9 measurement (n=1)

Ninth work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commits
`c288c09` (behavior) + `333d48a` (refactor). Window **2026-07-16T16:23:19Z → 16:37:20Z =
841s (14m1s)**.

| Artifact | What |
|---|---|
| `backend-3.1-gated-green-adapter-storage-wu9-timings.jsonl` | hook records, WU9 window (32) |
| `backend-3.1-gated-green-adapter-storage-wu9-progress.txt` | per-milestone stamped progress log |

**Untracked stops: 0.** 8 agents, 8 starts, 8 stops.

## Where the 841s went

| Phase | Wall | Share |
|---|---|---|
| **green-agent** | 103.2s | **12%** |
| Gap: green return → coverage dispatch | 30s | 4% |
| **`/test-coverage storage --focus`** | 153.6s | 18% |
| Behavior commit | 22s | 3% |
| Gap: commit → batch dispatch | 24s | 3% |
| **Refactor batch** — refactor 487.0s ‖ agent-review 231.3s ‖ premortem 237.6s | 487.0s | **58%** |
| Tail (refactor commit) | 21s | 2% |

## The finding: the cheapest agent of the entire run wrote the production code

**green-agent: 103.2s.** It is the cheapest agent of all 89 in the run, and it wrote
`H2TaskStorage` — the class the whole scenario exists to produce. It is **33% cheaper than
the coverage pass that audits it** (153.6s) and **4.7× cheaper than the `/refactor` that
tidies it** (487.0s).

Spine share for this unit: green + coverage = 256.8s = **31%**. WU4 established that the
spine is a minority of its own work unit; WU9 sharpens it — the primary agent alone is
**12%**. The remaining 88% is gates, review, refactor, and commit mechanics.

Across the run, all three `green-agent` runs total **385.1s — 2.2% of 17402.5s** of agent
work. Writing the production code is a rounding error.

## The first severity split: the same mechanism, two verdicts

The passes reached the **identical mechanism** and rated it differently — the run's first
such divergence:

| Pass | Verdict on `save()` → `em.merge()` (assigned `@Id`, no `@Version`) | Method |
|---|---|---|
| **agent-review** | **CONCERN** | ran the suite with `SPRING_JPA_SHOW_SQL=true` and **read the emitted SQL out of the results XML** — confirming the extra SELECT empirically |
| **premortem** | **REMOTE** | traced the overwrite path and found it needs a UUID collision to bite; returned **PASS** |

Premortem's PASS is the **only PASS of the entire run**.

Both are defensible: the mechanism is real (agent-review proved it emits an extra SELECT)
and the incident is remote (premortem proved it needs a UUID collision). The split is
about *reachability judgment*, not about facts — the same shape as WU12's later split over
the in-memory datasource. Two passes agreeing on mechanism and diverging on severity is
the healthy case; it is what surfacing rather than gating is for.

## `/refactor` corrected the previous unit's own lead — on two counts

WU8's refactor had left a direction for this unit. WU9's `/refactor` overrode it:

1. It is a **two-class import** (`@Import({H2TaskStorage.class, H2BoardStorage.class})`),
   not one.
2. "Converge onto the 1.1 precedent", read literally, **would have been a regression** —
   the task base autowires *ports*, the board base autowires the *concrete class*.
   Converging fully would have coupled the new test to an implementation type.

Only the bean-registration mechanism was converged. `StorageAccessTestConfiguration.java`,
created one unit earlier in WU8, was **deleted** here — a harness that did not survive a
single work unit.

At 487.0s, this `/refactor` is the longest of the run, and it swallowed both passes whole:
`serial_tail=0s`, marginal cost of the passes **0s**.

## A misaligned tally grep, caught

`/refactor`'s tally used a fixed-order attribute pattern against JUnit XML. **Attribute
order differs per file**, so the grep appeared to show the *wrong* class skipped. Re-queried
per-file rather than accepting the output. Third tooling defect of the run to come from
parsing test XML with line-oriented tools.

## Verdicts (both non-gating; commits stand)

- **agent-review: CONCERNS (1)** — the `merge()` mechanism above.
- **premortem: PASS** — 0 credible incidents. Three hypotheses opened and closed:
  unbounded `findAll()` per create (REMOTE — ADR bounds the board at ≤100 tasks),
  `merge()` vs `persist()` (REMOTE), `FakeTaskStorage` bean shadowing (REMOTE — it is
  unannotated and lives in `usecase/src/test`, so it never ships).

## Carry-over

- **Nothing was fixed.** Measurement run; the passes are non-gating.
- `coverage-agent` confirmed WU8's persistence-context finding **first-hand** here — the
  third independent instance of the executed-vs-pinned pattern.
- No backend was started — WU9 is **not** contaminated by the Task 18 Step 0 breakage.
- The per-milestone stamp addendum held with no misses.
