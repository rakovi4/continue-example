# Decision: Task creation — domain placement, insert-only port

**Date**: 2026-07-16 **Scenarios**: 3.1 (constrains 3.2, 4.1, 4.2, 5.1)

Why: 3.1 introduces the first write path; where position/column logic lives and
what the write port persists decides every later task scenario.

| Rejected | Why |
|----------|-----|
| Whole-aggregate `BoardStorage.save(board)` | Stale-overwrite by construction (concurrent create loses sibling task); destructive blast radius is every row, not the created one; O(N) write for an O(1) change |
| `TaskStorage.save` + usecase-computed position | Position/column invariants leak out of the domain; splits the Board aggregate across two ports |
| Controller-composed response (re-read board in REST) | Business composition in a first-layer adapter — forbidden by layer rules |

**Chosen**: `Board.addTask(title, description, now)` in the domain computes
position (bottom of To Do) and column, mints UUID id, returns the created
`Task`; usecase injects `java.time.Clock`, passes `Instant.now(clock)`
truncated to millis; new port method is insert-only `BoardStorage.saveTask(Task)`.
Controller maps the returned Task to flat `TaskResponse {id, title,
description, position, createdAt}` with 201.

## Model

- `Task`: + `id` (UUID), + `createdAt` (Instant, millis precision), + `position` (int, 1-based)
- `Board.addTask(Title, Description, Instant)`: places at bottom of To Do, throws `IllegalStateException` if no To Do column exists (loud, not fail-open)
- `CreateTaskUseCase`: + `Clock` (DI), + `BoardStorage` dependency; returns created `Task`
- `BoardStorage`: + `saveTask(Task)` — insert-only, single row, never touches neighbours
- Liquibase changeset: drop NOT NULL on `tasks.description` (additive; old code never writes null)
- `TaskResponse` (rest): flat JSON, `description` null when absent

## Edge Cases

| Case | Behavior |
|------|----------|
| Description absent in request | Task stores null description; response `description: null` (pinned by 3.1 acceptance test) |
| Board loaded without a To Do column | `Board.addTask` throws — explicit failure, no silent default branch |
| Concurrent create / retry duplicate | Accepted for now: POST is non-idempotent, single-user load (ExpectedLoad.md); race owned by load scenario 2.1 |

## Hazard scan record (design-preview step 2a)

All 8 catalogue groups dispatched, one pass each; group 8 (client/frontend)
dismissed as a block — out of altitude. Disposition of fired GAPs:

**Folded into this design** (guards land in 3.1 red phases):
- G4 NOT NULL collision → the Liquibase changeset above; 3.1 acceptance `description: null` assertion is the red guard
- G4 destructive whole-board save + G3 lost-update → insert-only `saveTask` port (neighbour-safe by construction); storage red asserts N rows + 1, neighbours byte-identical
- G2 transaction boundary → `saveTask` is a single-statement insert; atomic per DB, no partial aggregate write exists
- G7 uncontrollable `Instant.now()` → injected `Clock`; usecase red pins createdAt to a fixed clock instant
- G1 createdAt precision loss in transit → truncate to millis at creation; storage red asserts save→load equality
- G5 missing-To-Do fail-open → loud `IllegalStateException` invariant; domain-level test in usecase red
- G7 ISO-8601 wire format → already guarded: acceptance `AssertionHelpers` strictly `Instant.parse`s the raw field

**Owned by scheduled scenarios** (named, not silent):
- G5 mass assignment → security 2.1; G5 injection/XSS → security 1.1/1.2
- G3 position race under concurrency + G6 POST-at-capacity latency → load scenarios 2.1 / 03_Load_Tests phase
- G1 exact `position == 2` on non-empty board → scenario 4.2 work unit (upgrade its relative-order assertion to exact positions)
- G5 explicit `description: ""` vs null pinning → scenario 3.2 (description-valued create)

**Follow-ups recorded, no owner yet** (revisit at the named phase, do not drop):
- G6 capacity cap (reject 101st task) — enforce-or-decide at load phase
- G7 catch-all exception sanitization (storage failure leaks internals) — candidate addition to security phase
- G1 multibyte length semantics (UTF-16 units vs graphemes for the 100/5000 limits) — candidate coverage red, security 3.1 adjacency
- G4 unknown `column_type` enum row crashes board read — premature while the 3-column set is fixed; revisit with any column-adding story
