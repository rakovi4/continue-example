# Delete Task - API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| DELETE | /api/v1/tasks/{id} | Delete a task permanently |

## Notes

- Returns 204 No Content on success; 404 if task not found
- No request body — task ID in path is sufficient
- No authentication — single-user app
