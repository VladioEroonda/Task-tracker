package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;

public interface ProjectManagementService {

    /**
     * Закрытие Проекта
     *
     * @param projectClosingRequestDto (ProjectClosingRequestDto),
     *                                  представляет собой данные (id, статус) необходимые для закрытия Проекта
     * @return ProjectResponseDto - объект (обёртка) закрываемого Проекта
     */
    ProjectResponseDto closeProject(ProjectClosingRequestDto projectClosingRequestDto);
}
