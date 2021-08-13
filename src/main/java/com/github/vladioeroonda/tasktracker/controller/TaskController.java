package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.service.TaskService;
import com.github.vladioeroonda.tasktracker.service.impl.ProjectManagementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Задача", description = "Отвечает за CRUD операции, связанные с Задачей")
@RestController
@RequestMapping("/api/tracker/task")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Получение списка всех Задач")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        logger.info("GET /api/tracker/task");
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретной Задачи по её id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        logger.info("GET /api/tracker/task/{id}");
        TaskResponseDto task = taskService.getTaskByIdAndReturnResponseDto(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Operation(summary = "Добавление новой Задачи")
    @PostMapping
    public ResponseEntity<TaskResponseDto> addNewTask(@RequestBody TaskRequestDto requestDto) {
        logger.info("POST /api/tracker/task");
        TaskResponseDto task = taskService.addTask(requestDto);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @Operation(summary = "Удаление конкретной Задачи по её id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        logger.info("DELETE /api/tracker/task/{id}");
        taskService.deleteTask(id);
        return new ResponseEntity<>(
                String.format("Задача с id #%d была успешно удалена", id),
                HttpStatus.OK
        );
    }
}
