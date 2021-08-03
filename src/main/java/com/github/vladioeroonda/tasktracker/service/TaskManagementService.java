package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;

/**
 * Интерфейс управления задачей
 */
public interface TaskManagementService {

    /**
     * Обновление уже существующей Задачи(Смена статусов, значений полей)
     *
     * Выбрасываемые исключения:
     *
     * <li> TaskNotFoundException:
     * - если Задачи с указанным ID нет в БД.
     *
     * <li> ProjectNotFoundException:
     * - если Проекта с указанным в обновляемой задаче ID нет в БД.
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным в обновляемой задаче ID нет в БД.
     *
     * <li> UserNotFoundException:
     * - если Пользователя(и/или Исполнителя) с указанным в обновляемой задаче ID нет в БД.
     *
     * <li> TaskBadDataException
     * - если длина имени Задачи меньше, указанного в переменной task.min-length.name файла application.properties
     * - если длина описания Задачи меньше, указанного в переменной task.min-length.name файла application.properties
     * - Если Задача имеет статус отличный от BACKLOG и у неё не указан Исполнитель
     *
     *
     * @param taskRequestDto (TaskRequestDto), представляет собой обновляемую Задачу
     * @return TaskRequestDto - объект (обёртка) обновлённой Задачи
     */
    TaskResponseDto updateTask(TaskRequestDto taskRequestDto);
}
