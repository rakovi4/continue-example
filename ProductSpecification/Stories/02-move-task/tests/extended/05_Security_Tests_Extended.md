> These are additional edge case tests. Implement after core tests pass.

## 1. Path Traversal

### 1.1 Reject path traversal in task ID

```gherkin
When the user sends a move request with "../" in the task ID path
Then the request is rejected
```

## 2. Request Body Limits

### 2.1 Reject oversized request body

```gherkin
When the user sends a move request with a 1MB request body
Then the request is rejected
```
