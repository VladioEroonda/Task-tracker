package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.service.ProjectService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Проект", description = "Отвечает за CRUD операции, связанные с Проектом")
@RestController
@RequestMapping("/api/tracker/project")
public class ProjectController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(summary = "Получение списка всех Проектов")
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        logger.info("GET /api/tracker/project");
        List<ProjectResponseDto> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретного Проекта по его id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        logger.info("GET /api/tracker/project/{id}");
        ProjectResponseDto project = projectService.getProjectByIdAndReturnResponseDto(id);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Operation(summary = "Добавление нового Проекта")
    @PostMapping
    public ResponseEntity<ProjectResponseDto> addNewProject(@RequestBody ProjectRequestDto requestDto) {
        logger.info("POST /api/tracker/project/");
        ProjectResponseDto project = projectService.addProject(requestDto);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Проекта")
    @PutMapping
    public ResponseEntity<ProjectResponseDto> updateProject(@RequestBody ProjectRequestDto requestDto) {
        logger.info("PUT /api/tracker/project/");
        ProjectResponseDto project = projectService.updateProject(requestDto);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретного Проекта по его id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProjectById(@PathVariable Long id) {
        logger.info("DELETE /api/tracker/project/{id}");
        projectService.deleteProject(id);
        return new ResponseEntity<>(
                String.format("Проект с id #%d был успешно удалён", id),
                HttpStatus.OK
        );
    }
}
