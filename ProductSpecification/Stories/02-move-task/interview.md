## Scope

Story 2 covers moving tasks between columns and reordering tasks within a column on the Kanban board.

### In scope
- Move a task from any column to any other column (free movement, no workflow restrictions)
- Reorder a task within the same column (change position)
- Specify target position when moving/reordering
- Gap-free position reindexing after move/reorder operations
- Single API endpoint handles both move and reorder (same column = reorder)

### Out of scope
- Creating or deleting tasks (Stories 1 and 3)
- Editing task details (Story 3)
- Drag-and-drop UI mechanics (frontend implementation detail, not a domain concern)
- Multi-user / authentication
- Board or column management

## Key Architectural Decisions

DECISION: Single endpoint for move and reorder — `PATCH /api/v1/tasks/{id}` with `column_id` and `position`. Moving to the same column with a different position = reorder. Standard RESTful resource update — no RPC-style `/move` sub-resource, since Story 3 will extend the same endpoint for title/description edits.

DECISION: Free movement between all columns. No workflow enforcement — a task can go from Done back to To Do, or from To Do directly to Done.

DECISION: Target position is specified by the client. Valid range is `[0, columnSize]` where `columnSize` is the number of tasks already in the target column (excluding the moved task if same column).

DECISION: Gap-free position reindexing. When a task leaves a column, remaining tasks are reindexed to be contiguous (0, 1, 2...). When a task is inserted into a column at a given position, tasks at and after that position shift down.

DECISION: Out-of-range positions are clamped to the bottom. If position > columnSize, the task is appended at the end. Negative positions return 400.

DECISION: No-op moves (same column, same position) return 200 OK with no changes.

## Business Rules & Constraints

- Task must exist — 404 if task ID not found
- Target column must exist — 400 if column ID not found
- Position must be non-negative — 400 for negative values
- Position > columnSize → silently append to bottom
- After any move/reorder, all positions in affected columns are contiguous (0-based, no gaps)
- Both source and target columns are reindexed after the operation
- Moving to same column + same position = no-op, 200 OK

## Already Implemented (REUSE)

From Story 1 (dependency):
- Task entity with title, description, position, column reference — ALREADY IMPLEMENTED (by Story 1)
- Board with 3 columns (To Do, In Progress, Done) — ALREADY IMPLEMENTED (seed data)
- GET /api/v1/board — ALREADY IMPLEMENTED (returns board state for verifying moves)
- Task position tracking within columns — ALREADY IMPLEMENTED

## NOT Yet Implemented (Gaps)

- Domain: move logic (column change + position recalculation), reindex logic for gap-free positions
- Usecase: MoveTask usecase, port method for updating task column/position
- Adapters: PATCH /api/v1/tasks/{id} REST endpoint, storage update for column + position
- Frontend: drag-and-drop or move UI, API client for PATCH endpoint

## Cross-Story Dependencies

- **Depends on Story 1**: Task, Board, Column domain model; TaskStorage port; GET /api/v1/board endpoint
- **Story 3 depends on this**: Edit/Delete may need awareness of task positions (deleting a task triggers reindexing)
