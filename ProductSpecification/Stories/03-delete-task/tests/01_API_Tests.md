> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with invalid ID format, then delete happy path with position reordering, then delete non-existent task.

## 1. Delete Task — Validation

### 1.1 Reject invalid task ID format

```gherkin
When the user deletes a task with an invalid ID format
Then the request is rejected with a bad request error
And the error message indicates the task ID format is invalid
```

---

## 2. Delete Task — Happy Path

### 2.1 Delete a task from the board

```gherkin
Given a task "Obsolete task" exists in To Do
When the user deletes the task "Obsolete task"
Then the task is deleted successfully with no content returned
When the user requests the board
Then the To Do column does not contain "Obsolete task"
```

### 2.2 Remaining tasks reorder after deletion

```gherkin
Given tasks "First", "Second", "Third" exist in To Do in that order
When the user deletes the task "Second"
Then the task is deleted successfully
When the user requests the board
Then the To Do column contains "First" at position 1 and "Third" at position 2
```

---

## 3. Delete Task — Not Found

### 3.1 Delete a non-existent task

```gherkin
When the user deletes a task that does not exist
Then the request is rejected with a not found error
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task "X" exists in To Do` | POST /api/v1/tasks `{"title": "X"}` (setup) |
| `tasks "X", "Y", "Z" exist in To Do in that order` | POST /api/v1/tasks for each in sequence (setup) |
| `the user deletes the task "X"` | DELETE /api/v1/tasks/{id} using ID from setup |
| `the user deletes a task with an invalid ID format` | DELETE /api/v1/tasks/not-a-uuid |
| `the user deletes a task that does not exist` | DELETE /api/v1/tasks/{random-uuid} |
| `the task is deleted successfully with no content returned` | HTTP 204, no response body |
| `the task is deleted successfully` | HTTP 204 |
| `the request is rejected with a bad request error` | HTTP 400, error: "VALIDATION_ERROR" |
| `the request is rejected with a not found error` | HTTP 404, error about task not found |
| `the user requests the board` | GET /api/v1/board |
| `the To Do column does not contain "X"` | Board response To Do column has no task with title "X" |
| `the To Do column contains "X" at position 1 and "Y" at position 2` | Board response To Do tasks in order: "X" (pos 1), "Y" (pos 2) |
