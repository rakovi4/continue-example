package com.example.rest.controller.board;

import com.example.domain.board.Board;
import com.example.domain.board.Column;
import com.example.domain.board.ColumnType;
import com.example.rest.RestTest;
import com.example.rest.controller.board.BoardController;
import com.example.usecase.board.GetBoardUseCase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("1.1 Get empty board")
@WebMvcTest(BoardController.class)
class BoardControllerGetBoardTest implements RestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetBoardUseCase getBoardUseCase;

    @Test
    @SneakyThrows
    @DisplayName("""
            Given a board with three columns
            When the user requests the board
            Then the response contains the board with columns To Do, In Progress, Done
            And each column has no tasks""")
    void should_return_board_with_three_empty_columns() {
        when(getBoardUseCase.getBoard()).thenReturn(new Board(List.of(
                Column.empty(ColumnType.TO_DO),
                Column.empty(ColumnType.IN_PROGRESS),
                Column.empty(ColumnType.DONE)
        )));

        String expectedJson = new String(
                getClass().getResourceAsStream("/board/get-empty-board.json").readAllBytes()
        );

        mockMvc.perform(get("/api/v1/board"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}
