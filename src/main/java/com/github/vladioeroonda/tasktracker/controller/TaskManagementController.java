package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.service.TaskManagementService;
import com.github.vladioeroonda.tasktracker.service.impl.ProjectManagementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление Задачей", description = "Отвечает за управление Задачей (смена её параметров)")
@RestController
@RequestMapping("/api/tracker/task/management")
public class TaskManagementController {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagementController.class);

    private final TaskManagementService taskManagementService;

    public TaskManagementController(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }

    @Operation(summary = "Изменение Задачи")
    @PutMapping
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto requestDto) {
        logger.info("PUT /api/tracker/task/management");
        TaskResponseDto task = taskManagementService.updateTask(requestDto);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
