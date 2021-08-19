package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import com.github.vladioeroonda.tasktracker.service.TaskFilterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Фильтр задач", description = "Отвечает за фильтрацию задач по критериям")
@RestController
@RequestMapping("/api/tracker/task/filter")
public class TaskFilterController {
    private static final Logger logger = LoggerFactory.getLogger(TaskFilterController.class);

    private final TaskFilterService taskFilterService;

    public TaskFilterController(TaskFilterService taskFilterService) {
        this.taskFilterService = taskFilterService;
    }

    @Operation(summary = "Фильтрация задач")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<TaskResponseDto>> getFilteredTasks(
            @Parameter(description = "Имя задачи") @RequestParam(required = false) String name,
            @Parameter(description = "Описание задачи") @RequestParam(required = false) String description,
            @Parameter(description = "Статус задачи") @RequestParam(required = false) TaskStatus status,
            @Parameter(description = "Имя проекта в задаче") @RequestParam(required = false) String projectName,
            @Parameter(description = "Версия релиза в задаче") @RequestParam(required = false) String releaseVersion,
            @Parameter(description = "Имя автора в задаче") @RequestParam(required = false) String authorName,
            @Parameter(description = "Имя исполнителя в задаче") @RequestParam(required = false) String executorName
    ) {
        logger.info("GET /api/tracker/task/filter");
        List<TaskResponseDto> result = taskFilterService.getFilteredTasks(
                name, description, status, projectName,
                releaseVersion, authorName, executorName
        );
        return ResponseEntity.ok().body(result);
    }
}
