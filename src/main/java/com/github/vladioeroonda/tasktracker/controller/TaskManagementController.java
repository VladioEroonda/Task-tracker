package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.service.TaskManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление Задачей", description = "Отвечает за управление Задачей (смена её параметров)")
@RestController
@RequestMapping("/api/tracker/task/management")
public class TaskManagementController {

    private final TaskManagementService taskManagementService;

    public TaskManagementController(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }

    @Operation(summary = "Изменение Задачи")
    @PutMapping
    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto task = taskManagementService.changeTaskStatus(requestDto);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @ExceptionHandler(TaskBadDataException.class)
    public ResponseEntity handleNotFoundException(TaskBadDataException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
