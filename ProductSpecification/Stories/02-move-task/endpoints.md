# Move Task - API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| PATCH | /api/v1/tasks/{id} | Move or reorder a task |

## Notes

- Same endpoint handles both cross-column moves and within-column reorders
- Returns 200 with updated board state; 400 on invalid column/position; 404 on unknown task
- No authentication — single-user app
