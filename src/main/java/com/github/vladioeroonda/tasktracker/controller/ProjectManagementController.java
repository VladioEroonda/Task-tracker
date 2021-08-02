package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.service.ProjectManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление Проектом", description = "Отвечает за управление проектом")
@RestController
@RequestMapping("/api/tracker/project/management")
public class ProjectManagementController {

    private final ProjectManagementService projectManagementService;

    public ProjectManagementController(ProjectManagementService projectManagementService) {
        this.projectManagementService = projectManagementService;
    }

    @Operation(summary = "Завершение Проекта")
    @PatchMapping
    public ResponseEntity<ProjectResponseDto> closeProject(@RequestBody ProjectClosingRequestDto projectClosingRequestDto) {
        ProjectResponseDto project = projectManagementService.closeProject(projectClosingRequestDto);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }
}
