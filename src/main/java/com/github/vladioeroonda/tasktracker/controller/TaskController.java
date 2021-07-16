package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/tracker")
public class TaskController {

    private final ModelMapper modelMapper;

    @Autowired
    public TaskController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/tasks")
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

    @GetMapping(value = "/tasks/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {

        TaskResponseDto task = new TaskResponseDto(
                1L,
                "Task#1",
                TaskStatus.IN_PROGRESS
        );

        if (task == null) {
            throw new TaskNotFoundException("Задачи с id #" + id + " не существует");
        }

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping(value = "/tasks")
    public ResponseEntity<TaskResponseDto> addNewTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto task = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @PutMapping(value = "/tasks")
    public ResponseEntity<TaskResponseDto> editTask(@RequestBody TaskRequestDto requestDto) {
        TaskResponseDto task = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @DeleteMapping(value = "/tasks/{id}")
    public ResponseEntity<String> deleteTaskById(@PathVariable Long id) {
        String info = "Задача с id #" + id + " была успешно удалена";

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    private TaskResponseDto convertFromRequestToResponseDto(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, TaskResponseDto.class);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity handleNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
