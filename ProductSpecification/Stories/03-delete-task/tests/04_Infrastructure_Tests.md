> **Implementation Order**: Tests are numbered for sequential TDD implementation.

## 1. Database Failure

### 1.1 Delete task fails gracefully when database is unavailable

```gherkin
Given the database is unavailable
When the user deletes a task
Then the request fails with a server error
And the error message indicates a temporary failure
```

### 1.2 Delete task succeeds after database recovery

```gherkin
Given the database was unavailable and has recovered
And a task exists on the board
When the user deletes the task
Then the task is deleted successfully
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `the database is unavailable` | Stop H2 or simulate connection failure |
| `the database was unavailable and has recovered` | Restart H2 after simulated failure |
| `the user deletes a task` | DELETE /api/v1/tasks/{id} |
| `the request fails with a server error` | HTTP 500 |
| `the task is deleted successfully` | HTTP 204 |
