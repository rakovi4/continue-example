# Decision: Board.moveTask as Single Entry Point

**Date**: 2026-04-19 **Scenarios**: 1.1, 1.2, 1.3, 1.4, 2.1, 2.2, 2.3, 3.1, 4.1, 5.1

Move operation touches multiple columns (source removal, target insertion, reindexing) — must be coordinated by aggregate root.

| Rejected | Why |
|----------|-----|
| Public `findTaskById` + external move logic | Leaks aggregate internals, usecase becomes logic holder |
| MoveTaskUseCase orchestrates column operations | Violates "usecase is orchestrator, not logic holder" |
| Separate MoveService domain service | Board already owns columns and tasks — no need for extra indirection |

**Chosen**: `Board.moveTask(UUID, long, int)` — single method on aggregate root. Finds task, resolves target column, validates, moves, reindexes. Each scenario adds behavior incrementally.

## Model

- `Board.moveTask(UUID taskId, long columnId, int position)` — aggregate entry point for all move/reorder operations
- `TaskNotFoundException` extends `RuntimeException` — thrown when task ID not found across all columns, mapped to 404
- `MoveTaskUseCase(BoardStorage)` — loads board, calls `board.moveTask(...)`, saves
- `MoveTaskRequest(UUID taskId, long columnId, int position)` — usecase request DTO
- `TaskController` — new `PATCH /api/v1/tasks/{id}` endpoint, delegates to `MoveTaskUseCase`
- `GlobalExceptionHandler` — new handler: `TaskNotFoundException` -> 404 `TASK_NOT_FOUND`

## Edge Cases

| Case | Behavior |
|------|----------|
| Task not found | `TaskNotFoundException("Task not found")` -> 404 |
| Column not found (1.2) | `ValidationException` -> 400 |
| Negative position (1.3) | `ValidationException` -> 400 |
| Position > column size (4.1) | Clamped to bottom |
| Same column + same position (5.1) | No-op, returns 200 |
