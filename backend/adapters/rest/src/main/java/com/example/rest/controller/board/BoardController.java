package com.example.rest.controller.board;

import com.example.rest.dto.board.BoardResponseDto;
import com.example.usecase.board.GetBoardUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BoardController {

    private final GetBoardUseCase getBoardUseCase;

    @GetMapping("/board")
    public BoardResponseDto getBoard() {
        var board = getBoardUseCase.getBoard();
        return BoardResponseDto.from(board);
    }
}
