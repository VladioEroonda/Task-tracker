package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.service.ProjectManagementService;
import com.github.vladioeroonda.tasktracker.service.impl.ProjectManagementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление Проектом", description = "Отвечает за управление проектом")
@RestController
@RequestMapping("/api/tracker/project/management")
public class ProjectManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ProjectManagementController.class);

    private final ProjectManagementService projectManagementService;

    public ProjectManagementController(ProjectManagementService projectManagementService) {
        this.projectManagementService = projectManagementService;
    }

    @Operation(summary = "Завершение Проекта")
    @PatchMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProjectResponseDto> closeProject(@RequestBody ProjectClosingRequestDto projectClosingRequestDto) {
        logger.info("PATCH /api/tracker/project/management");
        ProjectResponseDto project = projectManagementService.closeProject(projectClosingRequestDto);
        return ResponseEntity.ok().body(project);
    }
}
