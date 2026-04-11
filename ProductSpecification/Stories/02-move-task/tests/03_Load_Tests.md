> **Implementation Order**: Tests are numbered for sequential TDD implementation.
> Start with single request baseline, then concurrent moves, then large board volume.

## 1. Response Time

### 1.1 Move task responds within 200ms

```gherkin
Given a board with tasks in multiple columns
When the user moves a task to another column
Then the response is received within 200ms
```

---

## 2. Concurrent Moves

### 2.1 Handle concurrent move requests

```gherkin
Given a board with 50 tasks across columns
When 50 move requests are sent concurrently
Then all responses are received within 500ms
And all board positions remain gap-free after completion
```

---

## 3. Volume

### 3.1 Move task on board with 100 tasks

```gherkin
Given a board with 100 tasks spread across three columns
When the user moves a task from one column to another
Then the response is received within 200ms
And positions in both columns are contiguous
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a board with tasks in multiple columns` | Setup: create and distribute tasks via API |
| `a board with 100 tasks` | Setup: bulk-create 100 tasks, distribute across columns |
| `the user moves a task to another column` | PATCH /api/v1/tasks/{id} |
| `50 move requests are sent concurrently` | Parallel PATCH requests with thread pool |
| `the response is received within 200ms` | Assert response time < 200ms |
| `all board positions remain gap-free` | GET /api/v1/board, verify 0-based contiguous positions |
