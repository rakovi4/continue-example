> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with DB failure handling, then recovery.

## 1. Database Failure

### 1.1 Move task fails gracefully on database error

```gherkin
Given a task exists in To Do
And the database connection is interrupted
When the user moves the task to In Progress
Then the move returns a server error
And no partial position updates persist
```

---

## 2. Database Recovery

### 2.1 Move task succeeds after database recovery

```gherkin
Given a task exists in To Do
And the database connection was interrupted and recovered
When the user moves the task to In Progress
Then the move completes successfully
And the board state is consistent
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task exists in To Do` | POST /api/v1/tasks setup |
| `the database connection is interrupted` | Simulate DB failure (connection pool kill) |
| `the database connection was interrupted and recovered` | Kill then restore DB connection |
| `the move returns a server error` | HTTP 500 |
| `no partial position updates persist` | GET /api/v1/board, verify original positions |
| `the board state is consistent` | GET /api/v1/board, verify gap-free positions |
