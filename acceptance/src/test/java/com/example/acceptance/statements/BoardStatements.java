package com.example.acceptance.statements;

import com.example.acceptance.clients.application.ApplicationClient;
import com.example.acceptance.clients.application.dto.board.BoardResponse;
import com.example.acceptance.clients.application.dto.board.ColumnResponse;
import com.example.acceptance.clients.application.dto.board.TaskSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.acceptance.statements.AssertionHelpers.assertTimestampRecent;
import static org.assertj.core.api.Assertions.assertThat;

@Service
@RequiredArgsConstructor
public class BoardStatements {

    private final ApplicationClient applicationClient;

    private BoardResponse lastBoardResponse;

    public void whenUserRequestsBoard() {
        lastBoardResponse = applicationClient.getBoard();
    }

    public void assertBoardHasThreeEmptyColumns() {
        assertThat(lastBoardResponse.getColumns())
                .as("board columns")
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new ColumnResponse("To Do", List.of()),
                        new ColumnResponse("In Progress", List.of()),
                        new ColumnResponse("Done", List.of())
                );
    }

    public void assertToDoContainsTask(String expectedId, String expectedTitle) {
        List<TaskSummaryResponse> tasks = findColumn("To Do").getTasks();
        assertThat(tasks).as("To Do task count").hasSize(1);
        assertTaskFields(tasks.get(0), expectedId, expectedTitle, 0);
    }

    private void assertTaskFields(TaskSummaryResponse task, String expectedId,
                                  String expectedTitle, int expectedPosition) {
        var expected = new TaskSummaryResponse(expectedId,
                new TaskSummaryResponse.ValueWrapper(expectedTitle),
                new TaskSummaryResponse.ValueWrapper(""), expectedPosition, null);
        assertThat(task).usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(expected);
        assertTimestampRecent(task.getCreatedAt(), "task creation timestamp");
    }

    public void assertToDoContainsTasksInOrder(String firstId, String firstTitle,
                                                String secondId, String secondTitle) {
        List<TaskSummaryResponse> tasks = findColumn("To Do").getTasks();
        assertThat(tasks).as("To Do task count").hasSize(2);
        assertTaskFields(tasks.get(0), firstId, firstTitle, 0);
        assertTaskFields(tasks.get(1), secondId, secondTitle, 1);
    }

    public void assertInProgressAndDoneEmpty() {
        ColumnResponse inProgress = findColumn("In Progress");
        ColumnResponse done = findColumn("Done");
        assertThat(inProgress.getTasks())
                .as("In Progress tasks")
                .isEmpty();
        assertThat(done.getTasks())
                .as("Done tasks")
                .isEmpty();
    }

    private ColumnResponse findColumn(String name) {
        return lastBoardResponse.getColumns().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Column not found: " + name));
    }
}
