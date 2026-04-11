package com.example.acceptance.clients.application.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private List<ColumnResponse> columns;
}
