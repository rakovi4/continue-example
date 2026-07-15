package com.example.domain.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Board {

    private final List<Column> columns;
}
