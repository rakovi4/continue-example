# Move Task

## Brief Description

User moves a task between columns or reorders it within a column. A single endpoint handles both operations — moving to the same column with a different position is a reorder.

## Flow

1. User sees the board with tasks in various columns
2. User initiates a move (drag-and-drop or move action)
3. Client sends PATCH with target column ID and position
4. System validates task exists, column exists, position is non-negative
5. Task is placed at the target position in the target column
6. Tasks in affected columns are reindexed to maintain gap-free positions
7. Board refreshes to show updated state

## Acceptance Criteria

- Task moves from any column to any other column (no workflow restrictions)
- Task can be reordered within the same column
- Position is 0-based; tasks shift down to accommodate insertion
- After move/reorder, all positions in affected columns are contiguous (0, 1, 2...)
- Out-of-range position is clamped to bottom of column
- Negative position returns 400
- Non-existent task returns 404
- Non-existent target column returns 400
- Same column + same position = no-op, returns 200 OK

## Validation Rules

| Field          | Rule                                        |
|----------------|---------------------------------------------|
| Task ID        | Must exist (404 if not found)               |
| Target Column  | Must exist (400 if not found)               |
| Position       | Non-negative integer (400 if negative)      |
| Position       | Clamped to column size if out of range      |

## Screen States

- Board view with tasks across multiple columns
- Task in new position after move
- Task in new order after reorder within column

## Core Requirements

- Single endpoint: `PATCH /api/v1/tasks/{id}` with `column_id` and `position`
- Both source and target columns reindexed after operation
- Reuses existing Task, Board, Column domain model from Story 1
- No authentication, single-user
