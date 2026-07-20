# green-acceptance 3.1, gated — WU12 measurement (n=1)

Twelfth and final work unit of the gated re-run, branch `benchmark/ceiling-old-3.1-gated`,
commit `0928513`. Window **2026-07-16T17:17:11Z → 17:30:14Z = 783s (13m3s)**. Single
commit — `green-acceptance` has no `/refactor` step. **Acceptance suite: 5 passed, 0
failed, 0 skipped. Scenario 3.1 complete.**

Dispatched **inline, no subagent**, per `/continue`'s rules — hence only 2 tracked agents
in the window, both review passes.

| Artifact | What |
|---|---|
| `backend-3.1-gated-green-acceptance-wu12-timings.jsonl` | hook records, WU12 window (10) |
| `backend-3.1-gated-green-acceptance-wu12-progress.txt` | per-milestone stamped progress log |

**Untracked stops: 2** in this window.

## Where the 783s went

| Phase | Wall | Share |
|---|---|---|
| **Inline work** — marker removal, jar build, boot attempt, diagnosis, boot, suite, commit | 423s | 54% |
| Gap: commit → passes dispatch | 15s | 2% |
| **Review passes** — agent-review 314.2s ‖ premortem 195.1s | **314.2s** | **40%** |
| Tail | 31s | 4% |

## The finding: the second pure serial tail, and it lands within 8% of the first

`green-acceptance` produces no `/refactor`, so its passes had **nothing to overlap** and
ran as a pure serial tail: **314.2s**. WU2 `design` — the only other step type with no
`/refactor` — paid **338.8s**.

Six units now, and the rule from WU3/WU4 is settled:

| `/refactor` present? | Units | Passes' marginal cost |
|---|---|---|
| **No** | WU2 `design`, WU12 `green-acceptance` | **338.8s, 314.2s** |
| Yes | WU3, WU4, WU5, WU8, WU9, WU10 | 0s – 102.8s |

**T6's lever is the batch, confirmed at n=6.** The passes are not expensive; they are
expensive *when nothing overlaps them*. Exactly two step types in the backend sequence pay
full price — `design` and `green-acceptance` — and they are precisely the two that produce
no refactorable delta. If T6 wants those 300s back, the fix is to give the passes something
to overlap, not to cut the passes.

## The scenario could not complete under its own rules

Removing the `@Disabled` marker was **not sufficient**. The context refused to start:

```
Parameter 2 of constructor in com.example.usecase.task.CreateTaskUseCase
required a bean of type 'java.time.Clock' that could not be found.
```

`grep -rn '@Bean' backend/` returned **nothing**. `green-acceptance` is
remove-marker-only, and **no earlier step in 3.1 had a home for the fix**. The unit
therefore shipped a **disclosed rule deviation**: a one-line `Clock.systemUTC()` bean in
`Application.java`. With it, the backend boots in ~4s and all five scenarios pass.

**The gate saw this coming.** WU3's `agent-review` reported "HEAD is an unbootable
application" nine work units earlier. The measurement protocol — never act on non-gating
findings — carried it to the end, by which point every test-writing window had closed.

### The arms diverge, and not for the reason it looks like

| | Spine-only arm (ungated) | Gated arm |
|---|---|---|
| `Clock` bean | wired in `Application.java` | never wired |
| Which step wired it | **`red-usecase`** (`ea4927d`) | — (landed here, out of contract) |
| Result | boots, 5/5 green | unbootable until this deviation |

The ungated arm completed **because its red-agent wrote production wiring during a RED
phase** — a red-phase discipline violation that nothing caught, because that arm had no
gates. The gated arm's red-agent respected the rule; its gate caught the consequence and
reported it; the protocol suppressed the fix.

**Both arms paid this cost out of contract. Only the gated arm's gate reported it.** The
divergence is an artifact of this run's do-not-fix protocol, **not** evidence that gates
cost more. Any new-arm run must handle `Clock` identically or the comparison is void.

## Two harness defects, both costing real minutes

- **`stop-backend.sh` is Windows-only** — `netstat … LISTENING` + `taskkill //PID`. On this
  Linux host it cannot stop anything. Same class as Task 18 Step 0.
- **`infrastructure/.env` has no `export` lines.** Sourcing it creates *shell* variables, so
  the Gradle test JVM never sees `BACKEND_URL` and `application-test.yml:2` falls back to
  its `http://localhost:8080` default. The suite then fails with `java.net.ConnectException:
  Connection refused` — a message that reads like a dead backend while the backend is up and
  healthy on the configured port. Cost a full diagnostic cycle. `set -a; . infrastructure/.env;
  set +a` is the fix.

## Verdicts (both non-gating; commit `0928513` stands)

This is the first commit in the run where the application **boots and serves real traffic**,
so both passes exercised live HTTP rather than reasoning. It produced the run's strongest
findings — and its **second severity split**.

- **agent-review: CONCERNS (4).** (1) `backend/application` — the module that wires the
  graph — **has no test source set at all**, and there is no `@SpringBootTest` anywhere in
  `backend/`; nothing guards context startup, so the *class* of bug recurs. (2)
  `ColumnNotFoundException` has no HTTP mapping → 500 with a leaked default body (latent:
  `buildColumns` iterates `ColumnType.values()`, so it is unreachable today). (3) **Verified
  live:** `POST /tasks` returns **two incompatible 400 body shapes** — malformed JSON gets
  Spring's default, validation failure gets `ErrorResponseDto`. (4) **Verified live:**
  `createdAt` differs between POST and GET for the same task (`…093597197Z` vs `…093597Z`).
- **premortem: BLOCK (3 credible)** — the run's only BLOCK. (1) `jdbc:h2:mem:testdb` is the
  only datasource in the repo → total data loss on restart and divergent boards across
  instances. (2) The `@Profile("!prod")` TRUNCATE endpoint is now **reachable** — and the
  guard is **vacuous**, not merely fail-open: the `prod` profile is configured in **zero**
  places repo-wide, so `!prod` is unconditionally true on every deployment path that exists.
  (3) **The board renders tasks in random order** — `findAll()` with no `ORDER BY` and a
  random-UUID PK returns rows in PK-index order, i.e. actively shuffled; no test reads a
  board with a task on it, and `AbstractBackendTest` truncates before each test, so
  `position` is only ever asserted at 0.

### The second severity split

The two passes reached the **same fact** about `jdbc:h2:mem:testdb` and split on what it
means: premortem rated it **BLOCK**; agent-review **declined to raise it**, reasoning that
the infrastructure phase has not run and no deployment exists, so it is plausibly the
intended template state. Same shape as WU9's `em.merge()` split — agreement on mechanism,
divergence on reachability. Orchestrator's read: **agent-review is better calibrated here**;
this is a template with nothing to deploy, and premortem's BLOCK is over-rated. Premortem's
**vacuous-`!prod`** fact, however, is new, sharp, and independently verified.

## An ADR claim falsified — the second of the run

The ADR pairs the `Clock` bean with `hibernate.jdbc.time_zone: UTC`; only the Clock half
shipped. agent-review **tested rather than reasoned**: on this genuine non-UTC host
(`Europe/Moscow`, +03:00) it round-tripped a task through the DB and observed **no hour
shift** — Boot 3.2.5 / Hibernate 6.4 maps `Instant` via `TIMESTAMP_UTC` regardless of the
setting. The config line is **inert today**; it becomes load-bearing only when the datasource
becomes durable or shared. This mirrors WU4's premortem falsifying the same ADR's claim that
the setting was load-bearing at the first write.

## Carry-over

- **Nothing was fixed** beyond the disclosed `Clock` deviation. The passes are non-gating.
- **The board-ordering defect is real, concrete, and one line to fix**
  (`findAllByOrderByPositionAsc`). It belongs to **4.1**, whose entire subject is reading a
  board with tasks; 3.1 is closed and its tests are read-only.
- **4.1 owes three fixtures on one door**: a non-empty task list (pins
  `ColumnResponseDto.from`'s mapping), a flat-shape acceptance client (`TaskSummaryResponse`
  → `TaskResponse`), and an array-order assertion (pins the ordering fix).
- The backend was started here — WU12 is the **only** unit of the run that is exposed to the
  Task 18 Step 0 breakage class, and it hit two harness defects because of it.
- The per-milestone stamp addendum held.
