package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
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

@Tag(name = "Проект", description = "Отвечает за CRUD операции, связанные с Проектом")
@RestController
@RequestMapping("/api/tracker/projects")
public class ProjectController {

    private final ModelMapper modelMapper;

    public ProjectController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Получение списка всех Проектов")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        ProjectResponseDto project = new ProjectResponseDto(
                1L,
                "First one",
                ProjectStatus.IN_PROGRESS,
                new UserResponseDto(1L, "ivan123", "Ivan Ivanov")
        );

        List<ProjectResponseDto> projects = List.of(project);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретного Проекта по его id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {

        ProjectResponseDto project = new ProjectResponseDto(
                1L,
                "First one",
                ProjectStatus.IN_PROGRESS,
                new UserResponseDto(1L, "ivan123", "Ivan Ivanov")
        );

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Operation(summary = "Добавление нового Проекта")
    @PostMapping
    public ResponseEntity<ProjectResponseDto> addNewProject(@RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto project = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Проекта")
    @PutMapping
    public ResponseEntity<ProjectResponseDto> editProject(@RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto project = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретного Проекта по его id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProjectById(@PathVariable Long id) {

        return new ResponseEntity<>(
                String.format("Проект с id #%s был успешно удалён", id),
                HttpStatus.OK
        );
    }

    private ProjectResponseDto convertFromRequestToResponseDto(ProjectRequestDto requestDto) {
        return modelMapper.map(requestDto, ProjectResponseDto.class);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity handleNotFoundException(ProjectNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
