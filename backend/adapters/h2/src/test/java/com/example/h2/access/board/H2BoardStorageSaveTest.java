package com.example.h2.access.board;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.task.Description;
import com.example.domain.task.Task;
import com.example.domain.task.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("3.1 Create task with title only — BoardStorage.save")
class H2BoardStorageSaveTest extends AbstractBoardStorageTest {

    @Test
    @DisplayName("""
            Given a board with a task in To Do
            When the board is saved and retrieved
            Then the task is present with correct title, description, position, and timestamp""")
    void should_persist_board_with_task() {
        Board board = h2BoardStorage.getBoard();
        Task task = board.addTask(new Title("Set up CI/CD"), new Description(""));

        h2BoardStorage.save(board);

        Board loaded = h2BoardStorage.getBoard();
        Column toDo = loaded.findToDoColumn();
        assertThat(toDo.getTasks()).as("tasks in To Do column").hasSize(1);
        assertTaskRoundTrip(toDo.getTasks().get(0), task);
    }

    private void assertTaskRoundTrip(Task actual, Task expected) {
        assertThat(actual)
                .as("persisted task round-trip")
                .usingRecursiveComparison()
                .withComparatorForType(
                        (a, b) -> a.truncatedTo(ChronoUnit.MINUTES)
                                .compareTo(b.truncatedTo(ChronoUnit.MINUTES)),
                        Instant.class)
                .isEqualTo(expected);
    }
}
