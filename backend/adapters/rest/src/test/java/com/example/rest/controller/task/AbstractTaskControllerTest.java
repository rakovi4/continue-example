package com.example.rest.controller.task;

import com.example.rest.RestTest;
import com.example.usecase.task.CreateTaskUseCase;
import com.example.usecase.task.MoveTaskUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

abstract class AbstractTaskControllerTest implements RestTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected CreateTaskUseCase createTaskUseCase;

    @MockBean
    protected MoveTaskUseCase moveTaskUseCase;
}
