> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> SQL injection on path/body, mass assignment, input validation.

## 1. SQL Injection

### 1.1 Reject SQL injection in task ID and request body

```gherkin
When the user sends a move request with SQL injection in the task ID
Then the request is rejected
When the user sends a move request with SQL injection in column_id
Then the request is rejected with a validation error
```

---

## 2. Mass Assignment

### 2.1 Ignore extra fields in move request

```gherkin
Given a task "Task A" exists in To Do
When the user sends a move request with extra fields (title, created_at)
Then only column_id and position are applied
And the task title and created_at remain unchanged
```

---

## 3. Input Validation

### 3.1 Reject non-integer values for position and column_id

```gherkin
When the user sends a move request with a string position
Then the request is rejected with a validation error
When the user sends a move request with a string column_id
Then the request is rejected with a validation error
```

### 3.2 Reject extremely large numeric values

```gherkin
Given a task exists in To Do
When the user sends a move request with position exceeding max integer
Then the request is rejected with a validation error
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `SQL injection in the task ID` | PATCH /api/v1/tasks/1;DROP TABLE |
| `SQL injection in column_id` | PATCH with `{"column_id": "1; DROP TABLE", "position": 0}` |
| `move request with extra fields` | PATCH with `{"column_id": N, "position": 0, "title": "hacked", "created_at": "..."}` |
| `a string position` | PATCH with `{"column_id": N, "position": "abc"}` |
| `position exceeding max integer` | PATCH with `{"column_id": N, "position": 999999999999}` |
| `only column_id and position are applied` | GET /api/v1/board, verify task moved but title unchanged |
