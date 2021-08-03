package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.model.Task;

import java.util.List;

/**
 * Интерфейс основных операций с Задачей
 */
public interface TaskService {

    /**
     * Получение списка всех существующих Задач
     *
     * @return Список Задач (обёртка)
     */
    List<TaskResponseDto> getAllTasks();

    /**
     * Получение конкретной Задачи по её ID и её возвращение в виде обёртки.
     * Используется для предоставления запрашиваемых данных пользователю.
     *
     * Выбрасываемые исключения:
     *
     * <li> TaskNotFoundException:
     * - если Задачи с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     * @return TaskResponseDto - объект Задачи (обёртка)
     */
    TaskResponseDto getTaskByIdAndReturnResponseDto(Long id);

    /**
     * Получение конкретной Задачи по её ID.
     * Используется для вызова в сервисах (не относящихся к Задаче напрямую)
     *
     * Выбрасываемые исключения:
     *
     * <li> TaskNotFoundException:
     * - если Задачи с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     * @return Task - объект Задачи
     */
    Task getTaskByIdAndReturnEntity(Long id);

    /**
     * Проверка на наличие конкретной Задачи по её ID
     *
     * Выбрасываемые исключения:
     *
     * <li> TaskNotFoundException:
     * - если Задачи с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     */
    void checkTaskExistsById(Long id);

    /**
     * Добавление новой Задачи
     *
     * Выбрасываемые исключения:
     *
     * <li> TaskBadDataException:
     * - если длина имени Задачи меньше, указанного в переменной task.min-length.name файла application.properties
     * - если длина описания Задачи меньше, указанного в переменной task.min-length.name файла application.properties
     * - если Задачу при создании пытаются отнести к уже закрытому Проекту
     * - если Задачу при создании пытаются отнести к уже закрытому Релизу
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
     *
     * @param taskRequestDto (TaskRequestDto), представляет собой новую Задачу
     * @return TaskRequestDto - объект (обёртка) добавленной Задачи
     */
    TaskResponseDto addTask(TaskRequestDto taskRequestDto);

    /**
     * Удаление конкретной Задачи по её ID
     *
     * Выбрасываемые исключения:
     *
     * <li> TaskNotFoundException:
     * - если Задачи с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Задачи
     */
    void deleteTask(Long id);

    /**
     * Получение числа незакрытых Задач по ID Релиза.
     * Используется при менеджменте Релиза.
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой ID Релиза
     * @return число незакрытых задач
     */
    int countUnfinishedTasksByReleaseId(Long id);

    /**
     * Смена статуса всех задач на Отменён по ID Релиза, к которому они относятся.
     * Используется при закрытии Релиза.
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой ID Релиза
     */
    void setAllTasksCancelled(Long id);
}
