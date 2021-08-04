package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;

/**
 * Интерфейс описывающий операции управления Проектом (закрытие)
 */
public interface ProjectManagementService {

    /**
     * Позволяет осуществить операцию закрытия/завершения Проекта
     *
     * Выбрасываемые исключения:
     *
     * <li> ProjectNotFoundException:
     * - если Проекта с указанным id нет в БД.
     *
     * <li> ProjectClosingException:
     * - если пользователь пытается закрыть Проект не статусом FINISHED.
     * - если пользователь пытается закрыть Проект, который уже в статусе FINISHED.
     * - если пользователь пытается закрыть Проект, в рамках которого остались незакрытые Релизы.
     *
     *
     * @param projectClosingRequestDto (ProjectClosingRequestDto),
     *                                 представляет собой данные необходимые для закрытия Проекта
     * @return ProjectResponseDto - объект (обёртка) закрываемого Проекта
     */
    ProjectResponseDto closeProject(ProjectClosingRequestDto projectClosingRequestDto);
}
