> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with validation errors, then cross-column move, then same-column reorder, then position clamping, then no-op move.

## 1. Move Task — Validation

### 1.1 Reject move of non-existent task

```gherkin
When the user moves a non-existent task to a column
Then the move is rejected with a not found error
```

### 1.2 Reject move to non-existent column

```gherkin
Given a task exists in To Do
When the user moves the task to a non-existent column
Then the move is rejected with a validation error
And the error message indicates the column was not found
```

### 1.3 Reject negative position

```gherkin
Given a task exists in To Do
When the user moves the task to In Progress at position -1
Then the move is rejected with a validation error
And the error message indicates the position must be non-negative
```

---

## 2. Move Task — Cross-Column

### 2.1 Move task before existing tasks in another column

```gherkin
Given task "Task A" exists in To Do
And tasks "Task X" and "Task Y" exist in In Progress
When the user moves "Task A" to In Progress at position 0
Then In Progress contains "Task A", "Task X", "Task Y" in order
And To Do is empty
```

### 2.2 Move task between existing tasks in another column

```gherkin
Given task "Task A" exists in To Do
And tasks "Task X" and "Task Y" exist in In Progress
When the user moves "Task A" to In Progress at position 1
Then In Progress contains "Task X", "Task A", "Task Y" in order
And To Do is empty
```

### 2.3 Move task after existing tasks in another column

```gherkin
Given task "Task A" exists in To Do
And tasks "Task X" and "Task Y" exist in In Progress
When the user moves "Task A" to In Progress at position 2
Then In Progress contains "Task X", "Task Y", "Task A" in order
And To Do is empty
```

---

## 3. Move Task — Same-Column Reorder

### 3.1 Reorder task within same column

```gherkin
Given tasks "Task A", "Task B", "Task C" exist in To Do in order
When the user moves "Task C" to To Do at position 0
Then To Do contains "Task C", "Task A", "Task B" in order
```

---

## 4. Move Task — Position Clamping

### 4.1 Clamp out-of-range position to bottom

```gherkin
Given task "Task A" exists in To Do
And task "Task X" exists in In Progress
When the user moves "Task A" to In Progress at position 99
Then In Progress contains "Task X", "Task A" in order
```

---

## 5. Move Task — No-Op

### 5.1 Same column and same position returns success

```gherkin
Given tasks "Task A", "Task B" exist in To Do in order
When the user moves "Task A" to To Do at position 0
Then the response is successful
And To Do still contains "Task A", "Task B" in order
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task exists in To Do` | POST /api/v1/tasks setup |
| `tasks "X" and "Y" exist in To Do` | POST /api/v1/tasks for each (setup) |
| `tasks "X" and "Y" exist in In Progress` | POST then PATCH to move (setup) |
| `the user moves a non-existent task to a column` | PATCH /api/v1/tasks/99999 `{"column_id": N, "position": 0}` |
| `the user moves the task to a non-existent column` | PATCH /api/v1/tasks/{id} `{"column_id": 99999, "position": 0}` |
| `the user moves the task to X at position N` | PATCH /api/v1/tasks/{id} `{"column_id": X_id, "position": N}` |
| `the user moves "X" to Y at position N` | PATCH /api/v1/tasks/{X_id} `{"column_id": Y_id, "position": N}` |
| `the move is rejected with a not found error` | HTTP 404, ErrorResponse |
| `the move is rejected with a validation error` | HTTP 400, ErrorResponse |
| `the response is successful` | HTTP 200, BoardResponse |
| `column contains "X", "Y" in order` | GET /api/v1/board, verify task positions |
