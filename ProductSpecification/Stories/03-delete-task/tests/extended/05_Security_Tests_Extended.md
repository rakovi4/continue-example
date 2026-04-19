> These are additional edge case tests. Implement after core tests pass.

## 1. Malformed Input

### 1.1 Reject oversized task ID in path

```gherkin
When the user deletes a task with an ID of 10000 characters
Then the request is rejected with a bad request error
```

### 1.2 Reject special characters in task ID

```gherkin
When the user deletes a task with ID containing null bytes and control characters
Then the request is rejected with a bad request error
```

---

## DSL Technical Reference

| DSL Statement | Technical Implementation |
|---------------|-------------------------|
| `a task with an ID of 10000 characters` | DELETE /api/v1/tasks/{10000-char-string} |
| `a task with ID containing null bytes and control characters` | DELETE /api/v1/tasks/{id-with-\0-and-\x01} |
