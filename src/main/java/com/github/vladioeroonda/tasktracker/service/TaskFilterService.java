package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;

import java.util.List;

public interface TaskFilterService {

    /**
     * Поиск(фильтрация) задач по заданным параметрам
     *
     * @param name           (String), представляет собой имя Задачи (может быть приблизительным)
     * @param description    (String), представляет собой описание Задачи (может быть приблизительным)
     * @param status         (TaskStatus), представляет собой статус Задачи (должен быть точным)
     * @param projectName    (String), представляет собой название Проекта (может быть приблизительным)
     * @param releaseVersion (String), представляет собой версию Релиза (должен быть точным)
     * @param authorName     (String), представляет собой имя автора Задачи (может быть приблизительным)
     * @param executorName   (String), представляет собой имя исполнителя Задачи (может быть приблизительным)
     * @return TaskResponseDto - список Задач (обёрток)
     */
    List<TaskResponseDto> getFilteredTasks(String name,
                                           String description,
                                           TaskStatus status,
                                           String projectName,
                                           String releaseVersion,
                                           String authorName,
                                           String executorName);
}
