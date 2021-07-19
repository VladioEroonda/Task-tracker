package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Задача", description = "Отвечает за CRUD операции, связанные с Задачей")
@RestController
@RequestMapping("/api/tracker/task")
public class TaskController {

    private final ModelMapper modelMapper;

    public TaskController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Получение списка всех Задач")
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        TaskResponseDto task1 = new TaskResponseDto(
                1L,
                "Task#1",
                TaskStatus.IN_PROGRESS
        );
        TaskResponseDto task2 = new TaskResponseDto(
                2L,
                "Task#2",
                TaskStatus.BACKLOG
        );

        List<TaskResponseDto> tasks = List.of(task1, task2);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретной Задачи по её id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {

        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Task#1",
                TaskStatus.IN_PROGRESS
        );

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Operation(summary = "Добавление новой Задачи")
    @PostMapping
    public ResponseEntity<TaskResponseDto> addNewTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto task = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Задачи")
    @PutMapping
    public ResponseEntity<TaskResponseDto> editTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto task = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретной Задачи по её id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {

        return new ResponseEntity<>(
                String.format("Задача с id #%s была успешно удалена", id),
                HttpStatus.OK
        );
    }

    private TaskResponseDto convertFromRequestToResponseDto(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, TaskResponseDto.class);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity handleNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
