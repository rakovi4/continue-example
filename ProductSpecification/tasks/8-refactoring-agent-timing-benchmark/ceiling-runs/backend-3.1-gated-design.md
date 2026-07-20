# design 3.1, gated — WU2 measurement (n=1)

Second work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`, commit
`1ad5418`. Window **2026-07-16T14:30:41Z → 14:49:24Z = 1123s (18m43s)**.

| Artifact | What |
|---|---|
| `backend-3.1-gated-design-wu2-timings.jsonl` | hook records, WU2 window (44) |
| `backend-3.1-gated-design-wu2-progress.txt` | per-milestone stamped progress log |

## Where the 1123s went

| Phase | Wall | Share |
|---|---|---|
| Preamble — read spec, scenario, code, ADRs, ExpectedLoad | 120s | 11% |
| **Hazard fan-out, 8 groups** (278.1s wall / **1041.1s agent work**) | 278.1s | 25% |
| Gap: fan-out end → synthesis dispatch | 50.2s | 4% |
| **Synthesis pass over 8 seams** | 239.0s | 21% |
| Draft ADR + progress + commit | 88.2s | 8% |
| **Review passes** (agent-review 217.0s ‖ premortem 332.5s) | 338.8s | 30% |

## The finding this unit produced: dispatch is not free

The 8 fan-out agents were dispatched in **one message**, but their `subagent_start`
records are staggered **~13.5s apart** — 14:32:41, :54, 14:33:08, :22, :38, :52, 14:34:07,
:21. **100s elapsed before the eighth agent began.** The stagger is the cost of *emitting*
each agent's prompt; the prompts here were long (each carried the full drafted design +
current code state inline).

That is 36% of the fan-out's 278.1s wall spent not-yet-parallel. Parallelism recovered
1041.1s of work into 278.1s wall (3.7×) — real, but capped by a serial prefix that scales
with prompt length × agent count. **A shorter prompt (design written to a file, agents
given the path) would shrink the prefix directly.** Worth testing against T3.

## Comparison to WU1

| | red-agent | `/test-review` | design |
|---|---|---|---|
| agent-review | — | — | 217.0s |
| premortem | — | — | 332.5s |

WU1's passes: agent-review 209.1s, premortem 178.2s. agent-review is stable across units
(209.1 → 217.0, +4%); **premortem swung +87% (178.2 → 332.5)** — it read more files (20 tool
uses) because an ADR full of accepted risks gave it more to chase than a test diff did.
The passes are 30% of this unit's wall and they are **non-gating** — they cannot stop the
commit. That is T6's target, not the spine's.

## Verdicts (both non-gating; commit `1ad5418` stands)

Both passes independently found the same defect, and it is a defect **in this unit's own
design work**:

- **The S3 read-back guard has no owner.** The ADR's centerpiece — "POST → GET /board,
  all five fields identical … retires three group claims" — cannot be written by any
  remaining 3.1 step: `red-acceptance` is already `[x]` and green phases treat tests as
  read-only. The synthesis pass named the guard; the sequence had already walked past the
  step that could write it. Two independent reviewers caught it; the synthesis pass did not.
- **`hibernate.jdbc.time_zone: UTC` is recorded under a key Spring will not read** (needs
  the `spring.jpa.properties.` prefix) — and is unobservable anyway: a same-JVM round-trip
  is symmetric, so no acceptance or storage test can see a zone error.
- **`id`'s origin is never fixed**, though the ADR's first line claims it fixes it —
  `addToToDo` takes `UUID id` as a parameter with no source, while `TaskEntity` has no
  `@GeneratedValue`, so `save()` is `merge()` (upsert, not insert).

## Carry-over

- The per-milestone stamp addendum worked again: the 13.5s dispatch stagger is only visible
  because the hook records exist, and the phase boundaries only because agents stamped live.
- **Nothing was fixed.** The passes are non-gating and this is a measurement run — the
  three findings are recorded here as WU2's output, not applied. A real (non-benchmark) run
  would owe the user a decision on the orphaned S3 guard.
- No backend was started this unit — design touches no infrastructure. WU2 is therefore
  **not** contaminated by the `/test-acceptance` Linux breakage (Task 18 Step 0); its 1123s
  is a clean number.
