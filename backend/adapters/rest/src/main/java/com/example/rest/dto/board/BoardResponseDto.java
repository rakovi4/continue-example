package com.example.rest.dto.board;

import com.example.domain.board.Board;
import lombok.Value;

import java.util.List;

@Value
public class BoardResponseDto {

    List<ColumnResponseDto> columns;

    public static BoardResponseDto from(Board board) {
        return new BoardResponseDto(
                board.getColumns().stream().map(ColumnResponseDto::from).toList()
        );
    }
}
