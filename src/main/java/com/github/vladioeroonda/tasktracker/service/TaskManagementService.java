package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface TaskManagementService {

    /**
     * Обновление уже существующей Задачи
     *
     * @param taskRequestDto (TaskRequestDto), представляет собой обновляемую Задачу
     * @return TaskRequestDto - объект (обёртка) обновлённой Задачи
     */
    TaskResponseDto closeTask(TaskRequestDto taskRequestDto);
}
