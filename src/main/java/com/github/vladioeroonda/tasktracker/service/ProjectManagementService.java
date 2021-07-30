package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectStatusChangingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;

public interface ProjectManagementService {

    /**
     * Закрытие Проекта
     *
     * @param projectStatusChangingRequestDto (ProjectStatusChangingRequestDto),
     *                                        представляет собой данные (id, статус) необходимые для закрытия Проекта
     * @return ProjectResponseDto - объект (обёртка) закрываемого Проекта
     */

    ProjectResponseDto closeProject(ProjectStatusChangingRequestDto projectStatusChangingRequestDto);

}
