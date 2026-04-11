package com.example.h2.access.board;

import com.example.h2.H2Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(H2BoardStorage.class)
abstract class AbstractBoardStorageTest implements H2Test {

    @Autowired
    protected H2BoardStorage h2BoardStorage;
}
