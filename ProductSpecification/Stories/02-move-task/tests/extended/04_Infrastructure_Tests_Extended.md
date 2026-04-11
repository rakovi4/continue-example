> These are additional edge case tests. Implement after core tests pass.

## 1. Transactional Integrity

### 1.1 Move operation is atomic

```gherkin
Given tasks exist in To Do and In Progress
When the database fails mid-move (after removing from source, before inserting to target)
Then no positions are changed in either column
And the board state matches pre-move state
```
