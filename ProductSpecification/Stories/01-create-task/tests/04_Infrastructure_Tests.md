> **Implementation Order**: Tests are numbered for sequential TDD implementation.

## 1. Database Resilience

### 1.1 Task creation fails gracefully on database error

```gherkin
Given the database is unavailable
When the user creates a task with title "Test task"
Then the response indicates a server error
And no partial data is persisted
```

### 1.2 Board retrieval after database recovery

```gherkin
Given the database was temporarily unavailable
And the database has recovered
When the user requests the board
Then the board is returned with the correct state
```
