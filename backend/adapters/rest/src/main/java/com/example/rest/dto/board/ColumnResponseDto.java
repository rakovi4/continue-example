package com.example.rest.dto.board;

import com.example.domain.board.Column;
import lombok.Value;

import java.util.List;

@Value
public class ColumnResponseDto {

    String name;
    List<Object> tasks;

    public static ColumnResponseDto from(Column column) {
        return new ColumnResponseDto(column.getName(), List.copyOf(column.getTasks()));
    }
}
