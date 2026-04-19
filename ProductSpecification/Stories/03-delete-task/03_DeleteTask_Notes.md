# Delete Task - Notes & Considerations

## Warnings

### Functional Warnings
- Position reordering must be atomic with deletion in the domain layer -- never persist a column with position gaps
- If task ID format is invalid (not a UUID), return 400 not 404

### UI/UX Warnings
- Delete button placement must be unambiguous -- avoid accidental clicks near other interactive elements
- Confirmation dialog must be blocking (user cannot interact with board until dismissed)

### Technical Warnings
- The Board aggregate is saved as a whole via `BoardStorage.save(Board)` -- deletion and reordering happen in domain, then entire board is persisted
- No orphan cleanup needed since JPA cascade handles task removal when board is saved

---

## Suggestions & Future Enhancements

### Functional Suggestions
- Undo deletion (snackbar with "Undo" for N seconds) -- explicitly out of scope
- Bulk delete -- out of scope

### UI/UX Suggestions
- Keyboard shortcut for delete (e.g., selecting card + pressing Delete key)
- Toast/snackbar confirmation after successful deletion

---

## Technical Notes

### Load Considerations
- Max 100 tasks, single user -- no performance concerns
- Position reordering is O(n) where n = tasks in column -- negligible at this scale

### Security Considerations
- Validate UUID format on input to prevent injection
- No authorization needed (single-user app)

### Infrastructure Notes
- No new database migrations needed -- existing schema supports deletion via cascade
- No new H2 adapter methods needed -- BoardStorage.save() handles the updated board state

### Integration Notes
- No external API dependencies
- No cross-story interaction with Move (Story 2) or Edit (Story 4)

---

## Additional Context

- See `interview.md` for full interview with architectural decisions
- Key decision: confirmation is frontend-only (browser dialog), no server-side confirmation state
- Reuses existing Board aggregate pattern from Story 1
