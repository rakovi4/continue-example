> These are additional edge case tests. Implement after core tests pass.

## 1. Partial Failure

### 1.1 Database fails mid-delete does not leave orphan data

```gherkin
Given a task exists on the board
And the database fails during the delete operation
When the database recovers
Then the task either exists fully or is fully deleted
And no partial state remains
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `the database fails during the delete operation` | Simulate connection drop mid-transaction |
| `no partial state remains` | GET /api/v1/board shows consistent state |
