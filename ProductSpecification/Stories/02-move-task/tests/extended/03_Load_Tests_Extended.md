> These are additional edge case tests. Implement after core tests pass.

## 1. Sustained Load

### 1.1 Sustained move operations over time

```gherkin
Given a board with 50 tasks
When 100 sequential move operations are performed over 60 seconds
Then average response time stays below 200ms
And no request times out
```
