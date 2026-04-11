# Create Task - Notes & Considerations

## Warnings

### Functional Warnings
- Duplicate title check is case-sensitive: "My Task" and "my task" are different. Consider if this could confuse users.
- Max 100 tasks limit (from ExpectedLoad) is not enforced at the application level in this story — it's a capacity guideline, not a hard validation rule.

### UI/UX Warnings
- Form should clear after successful submission — don't leave stale data.
- Error messages for validation failures should be specific (not generic "invalid input").
- Board should show empty state gracefully when no tasks exist yet.

### Technical Warnings
- Task position/ordering must be persisted — don't rely on insertion order from DB queries without an explicit position field.
- Seed data migration must be idempotent — running it twice should not create duplicate boards/columns.

---

## Suggestions & Future Enhancements

### Functional Suggestions
- Case-insensitive duplicate check could be added later if users find case-sensitive matching confusing.
- Bulk task creation (paste multiple titles) could be a future enhancement.

### UI/UX Suggestions
- Keyboard shortcut for quick task creation (e.g., Ctrl+Enter to submit).
- Auto-focus on title field when creation form opens.

### Technical Suggestions
- Consider using a gap-based positioning strategy (e.g., positions 100, 200, 300) to allow future reordering without renumbering all tasks.

---

## Technical Notes

### Load Considerations
- Single-user, max 100 tasks — no performance concerns for this story.
- No pagination needed given the 100-task ceiling.

### Security Considerations
- No authentication in this story — single-user app.
- Sanitize title and description inputs to prevent XSS if rendered as HTML.
- Title length validation prevents oversized payloads.

### Infrastructure Notes
- H2 in-memory database for development — Liquibase manages schema and seed data.
- Board + 3 columns seeded via Liquibase changelog (not application code).

---

## Additional Context

- See `interview.md` for full interview decisions.
- This is the foundational story — the domain model (Task, Board, Column) established here will be reused by Stories 2 (Move Task) and 3 (Edit/Delete Task).
