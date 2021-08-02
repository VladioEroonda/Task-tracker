package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.model.Task;

import java.util.List;

/**
 * Интерфейс (сервисного слоя) операций с Задачей
 */
public interface TaskService {

    /**
     * Получение списка всех существующих Задач
     *
     * @return List Задач (обёртку)
     */
    List<TaskResponseDto> getAllTasks();

    /**
     * Получение конкретной Задачи по её ID и её возвращение в виде обёртки
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     * @return TaskResponseDto - объект Задачи (обёртка)
     */
    TaskResponseDto getTaskByIdAndReturnResponseDto(Long id);

    /**
     * Получение конкретной Задачи по её ID
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     * @return Task - объект Задачи
     */
    Task getTaskByIdAndReturnEntity(Long id);

    /**
     * Проверка на наличие конкретной Задачи по её ID
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     */
    void checkTaskExistsById(Long id);

    /**
     * Добавление новой Задачи
     *
     * @param taskRequestDto (TaskRequestDto), представляет собой новую Задачу
     * @return TaskRequestDto - объект (обёртка) добавленной Задачи
     */
    TaskResponseDto addTask(TaskRequestDto taskRequestDto);

    /**
     * Удаление конкретной Задачи по её ID
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     */
    void deleteTask(Long id);
}
