## Scope

Story 3 covers deleting a task from the Kanban board. Any task in any column (To Do, In Progress, Done) can be deleted. Deletion is permanent — no soft delete or archive.

### In scope
- Delete task by ID (happy path)
- Delete task that doesn't exist (404 error)
- Frontend confirmation dialog before sending DELETE request
- Position reordering after delete (close gaps in column)

### Out of scope
- Backend-side confirmation (token-based) — confirmation is frontend-only (modal/dialog)
- Soft delete / archive / undo
- Bulk delete
- Restrictions based on column/status — any task can be deleted from any column

## Key Architectural Decisions

DECISION: Confirmation is frontend-only. Browser shows a confirmation modal before sending the DELETE request. Backend receives a single DELETE call and executes immediately — no confirmation state server-side.

DECISION: After a task is deleted, remaining tasks in the same column reorder to close position gaps. If tasks had positions 1, 2, 3 and task at position 2 is deleted, remaining tasks become 1, 2 (not 1, 3).

DECISION: Delete modifies the Board aggregate in the domain layer, then saves via existing `BoardStorage.save(Board)`. No new H2 adapter storage methods needed — the existing save-whole-board approach handles it.

## Business Rules & Constraints

- Any task can be deleted regardless of which column it's in
- Deleting a non-existent task returns 404
- Positions reorder within the column after deletion (no gaps)
- Single-user app — no concurrency concerns for delete

## Already Implemented (REUSE)

- Task entity with id, title, description, position, createdAt — ALREADY IMPLEMENTED
- Board/Column domain model with 3 columns — ALREADY IMPLEMENTED
- BoardStorage.getBoard() and BoardStorage.save(Board) — ALREADY IMPLEMENTED
- H2 persistence (TaskEntity, JpaRepository, H2BoardStorage) — ALREADY IMPLEMENTED
- REST controller base at /api/v1 — ALREADY IMPLEMENTED
- GlobalExceptionHandler — ALREADY IMPLEMENTED
- Board view frontend — ALREADY IMPLEMENTED (Story 1)

## NOT Yet Implemented (Gaps)

- Domain: delete method on Board/Column that removes task and reorders positions
- Domain: TaskNotFoundException (or similar) for missing task ID
- Usecase: DeleteTaskUseCase
- REST: DELETE /api/v1/tasks/{id} endpoint returning 204
- Frontend: delete button on task cards
- Frontend: confirmation modal/dialog before delete
- Frontend: API client for DELETE call

## Cross-Story Dependencies

- Depends on Story 1 (Create Task) for the existing Task/Board model, storage, and board view
- Story 2 (Move Task) is independent — delete doesn't interact with move in any special way
- Story 4 (Edit Task) is independent — no dependency in either direction
