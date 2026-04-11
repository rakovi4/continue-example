# Move Task - Notes & Considerations

## Warnings

### Functional Warnings
- Reindexing must handle both source and target columns — when moving between columns, the source column has a gap to close and the target column needs to shift tasks down.
- Same-column reorder: the task is removed from its old position first, then inserted at the new position. The effective index depends on whether old position < new position.
- Clamping out-of-range positions means the client can send position=999 and get the task appended — tests should verify actual resulting position.

### UI/UX Warnings
- Board should reflect updated positions immediately after a move — stale positions cause visual jumps on next refresh.
- Drag-and-drop must calculate the correct target position based on drop location relative to existing tasks.

### Technical Warnings
- Position reindexing must be atomic — if the app crashes mid-reindex, positions could have gaps or duplicates. Use @Transactional.
- Concurrent moves are not a concern (single-user app per ExpectedLoad), but the reindexing logic should still be correct under sequential rapid moves.

---

## Suggestions & Future Enhancements

### Functional Suggestions
- Workflow enforcement (e.g., must go To Do -> In Progress -> Done) could be added later as a board setting.
- Move history / activity log could track task movement for analytics.

### Technical Suggestions
- Batch position update (single UPDATE with CASE/WHEN) could replace individual saves if performance matters — but with max 100 tasks, iterative saves are fine.

---

## Technical Notes

### Load Considerations
- Single-user, max 100 tasks — worst case reindex touches ~99 tasks in one column. No performance concern.
- No need for optimistic locking given single-user constraint.

### Security Considerations
- No authentication in this story.
- Validate that task ID and column ID are valid numeric/UUID values to prevent injection.
- Position field should be validated as integer to reject non-numeric input.

### Infrastructure Notes
- Reuses H2 database and Liquibase schema from Story 1.
- No new migrations needed for the move operation — task already has column reference and position fields.

---

## Additional Context

- See `interview.md` for full scope decisions and architectural choices.
- Depends on Story 1's domain model (Task, Board, Column) and seed data (3 columns).
- Story 3 (Edit/Delete) will need similar reindexing logic when a task is deleted.
