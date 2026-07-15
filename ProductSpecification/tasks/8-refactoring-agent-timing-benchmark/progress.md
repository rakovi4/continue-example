# Task 8: Agent Timing Instrumentation + Ceiling Benchmark -- Progress

Type: refactoring

## Spec
- [x] spec

## Streams

Work is split into two parallel streams (see
[framework-speedup/dependencies.md](../../framework-speedup/dependencies.md),
wave 1). This file is the single source of truth **as an index**: which streams
exist and who claimed them. Per-stream state lives in the stream files.

| Stream | File | Scope | Claimed by |
|---|---|---|---|
| hooks | [progress-hooks.md](progress-hooks.md) | Steps 1–2: timing hooks + aggregation | — |
| fixtures | [progress-fixtures.md](progress-fixtures.md) | Steps 3–4: fixture replay + ceiling baselines | — |

The task is done when every checkbox in both stream files is `[x]` (or `[S]`).
Step 4 (fixtures stream) is blocked until the hooks stream completes — the
baseline runs must be captured by the timing hooks.
