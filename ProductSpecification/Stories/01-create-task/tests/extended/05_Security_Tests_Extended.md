> These are additional edge case tests. Implement after core tests pass.

### 1.1 Malformed JSON request body

```gherkin
When the user sends a malformed JSON body to the task creation endpoint
Then the response indicates a bad request error
And no task is created
```

### 1.2 Oversized request body

```gherkin
When the user sends a request body exceeding 1MB to the task creation endpoint
Then the response indicates a payload too large error
```
