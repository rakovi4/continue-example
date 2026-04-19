# Delete Task

## Brief Description

User deletes a task from any column on the board. A confirmation dialog appears before the task is permanently removed. Remaining tasks in the column reorder to close position gaps.

## Flow

1. User sees a task card on the board (any column)
2. User clicks the delete button on the task card
3. Confirmation dialog asks "Are you sure?"
4. User confirms deletion
5. Frontend sends DELETE /api/v1/tasks/{id}
6. Backend removes task from the column and reorders positions
7. Backend returns 204 No Content
8. Board refreshes to show updated column

## Acceptance Criteria

- Any task can be deleted regardless of column (To Do, In Progress, Done)
- Confirmation dialog shown before sending DELETE request
- Cancelling the dialog does not delete the task
- After deletion, remaining tasks in the column reorder (no position gaps)
- Deleting a non-existent task returns 404
- Deletion is permanent (no undo, no archive)

## Validation Rules

| Field   | Rule                        |
|---------|-----------------------------|
| Task ID | Required, must exist (UUID) |

## Screen States

- Task card with delete button (default state)
- Confirmation dialog (modal or browser confirm)
- Board view after successful deletion (task gone, positions compacted)

## Core Requirements

- DELETE /api/v1/tasks/{id} endpoint returns 204 on success, 404 if not found
- Domain: Board/Column removes task and reorders remaining positions
- No new storage methods needed (existing BoardStorage.save handles it)
- Frontend-only confirmation (no server-side confirmation state)
- Single-user, no concurrency concerns
