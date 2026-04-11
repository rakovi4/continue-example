> These are additional edge case tests. Implement after core tests pass.

### 1.1 Concurrent task creation with same title

```gherkin
When two users simultaneously create a task with title "Race condition"
Then exactly one task is created
And the other request is rejected with a duplicate title error
```
