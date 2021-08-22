package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        logger.info("GET /api/tracker/task");
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok().body(tasks);
    }

    @Operation(summary = "Получение конкретной Задачи по её id")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        logger.info("GET /api/tracker/task/{id}");
        TaskResponseDto task = taskService.getTaskByIdAndReturnResponseDto(id);
        return ResponseEntity.ok().body(task);
    }

    @Operation(summary = "Добавление новой Задачи")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskResponseDto> addNewTask(@RequestBody TaskRequestDto requestDto) {
        logger.info("POST /api/tracker/task");
        TaskResponseDto task = taskService.addTask(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Добавление новых Задач(и) через CSV-файл")
    @PostMapping(value = "/csv")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskResponseDto>> addNewTaskByCsv(@RequestParam("file") MultipartFile file) {
        logger.info("POST /api/tracker/task");
        List<TaskResponseDto> tasks = taskService.addTaskByCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
    }

    @Operation(summary = "Удаление конкретной Задачи по её id")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        logger.info("DELETE /api/tracker/task/{id}");
        taskService.deleteTask(id);
        return ResponseEntity.ok().body(String.format("Задача с id #%d была успешно удалена", id));
    }
}
