package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.model.Project;

import java.util.List;

/**
 * Интерфейс (сервисного слоя) операций с Проектом
 */
public interface ProjectService {

    /**
     * Получение списка всех существующих Проектов
     *
     * @return List Проектов (обёртку)
     */
    List<ProjectResponseDto> getAllProjects();

    /**
     * Получение конкретного Проекта по его ID и возвращение в виде обёртки
     *
     * @param id (Long), представляет собой уникальный ID Проекта
     * @return ProjectResponseDto - объект Проекта (обёртка)
     */
    ProjectResponseDto getProjectByIdAndReturnResponseDto(Long id);

    /**
     * Получение конкретного Проекта по его ID
     *
     * @param id (Long), представляет собой уникальный ID Проекта
     * @return Project - объект Проекта
     */
    Project getProjectByIdAndReturnEntity(Long id);

    /**
     * Проверка на наличие конкретного Проекта по его ID
     *
     * @param id (Long), представляет собой уникальный ID Проекта
     */
    void checkProjectExistsById(Long id);

    /**
     * Добавление нового Проекта
     *
     * @param projectRequestDto (ProjectRequestDto), представляет собой новый Проект
     * @return ProjectResponseDto - объект (обёртка) добавленного Проекта
     */
    ProjectResponseDto addProject(ProjectRequestDto projectRequestDto);

    /**
     * Обновление уже существующего Проекта. P.s. Данный метод не позволяет закрыть Проект.
     *
     * @param projectRequestDto (ProjectRequestDto), представляет собой обновляемый Проект
     * @return ProjectResponseDto - объект (обёртка) обновлённого Проекта
     */
    ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto);

    /**
     * Удаление конкретного Проекта по его ID
     *
     * @param id (Long), представляет собой уникальный ID Проекта
     */
    void deleteProject(Long id);
}
