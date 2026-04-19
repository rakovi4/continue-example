> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Focus: input validation on task ID path parameter. No auth (single-user app), no user input body (DELETE has no body).

## 1. Input Validation

### 1.1 Reject SQL injection in task ID path parameter

```gherkin
When the user deletes a task with ID containing SQL injection payload
Then the request is rejected with a bad request error
And the database is not affected
```

### 1.2 Reject path traversal in task ID

```gherkin
When the user deletes a task with ID containing path traversal characters
Then the request is rejected with a bad request error
```

---

## 2. IDOR Protection

### 2.1 Cannot enumerate task IDs via delete responses

```gherkin
When the user sends delete requests with sequential numeric IDs
Then all requests are rejected with a bad request error
And the response does not reveal whether a task exists at that ID
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task with ID containing SQL injection payload` | DELETE /api/v1/tasks/1'; DROP TABLE tasks;-- |
| `a task with ID containing path traversal characters` | DELETE /api/v1/tasks/../../../etc/passwd |
| `delete requests with sequential numeric IDs` | DELETE /api/v1/tasks/1, /api/v1/tasks/2, etc. |
| `the request is rejected with a bad request error` | HTTP 400 |
| `the database is not affected` | GET /api/v1/board returns expected state |
| `the response does not reveal whether a task exists` | Same error format for existent and non-existent numeric IDs |
