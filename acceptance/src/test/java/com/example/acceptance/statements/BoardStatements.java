package com.example.acceptance.statements;

import com.example.acceptance.clients.application.ApplicationClient;
import com.example.acceptance.clients.application.dto.board.BoardResponse;
import com.example.acceptance.clients.application.dto.board.ColumnResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
