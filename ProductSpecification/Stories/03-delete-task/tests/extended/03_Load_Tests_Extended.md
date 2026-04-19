> These are additional edge case tests. Implement after core tests pass.

## 1. Rapid Sequential Deletes

### 1.1 Delete all tasks from a column sequentially

```gherkin
Given 50 tasks exist in To Do
When the user deletes all 50 tasks one by one
Then each delete completes within 200 milliseconds
And the To Do column is empty
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `50 tasks exist in To Do` | POST /api/v1/tasks x50 (setup) |
| `the user deletes all 50 tasks one by one` | Sequential DELETE /api/v1/tasks/{id} for each |
