package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Task;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.TaskManagementService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagementServiceImpl.class);

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ReleaseRepository releaseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskManagementServiceImpl(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            ReleaseRepository releaseRepository,
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.releaseRepository = releaseRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public TaskResponseDto changeTaskStatus(TaskRequestDto taskRequestDto) {
        logger.info("Выполнение Задачи (изменение полей)");

        fieldsCheckForExisting(taskRequestDto);

        if (!taskRequestDto.getStatus().equals(TaskStatus.BACKLOG) && taskRequestDto.getExecutor() == null) {
            TaskBadDataException exception =
                    new TaskBadDataException(String.format(
                            "Задаче id #%d со статусом отличным от %s не назначен Исполнитель", taskRequestDto.getId(), TaskStatus.BACKLOG)
                    );
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        Task taskForSave = convertFromRequestToEntity(taskRequestDto);
        Task updatedTask = taskRepository.save(taskForSave);
        return convertFromEntityToResponse(updatedTask);
    }

    private void fieldsCheckForExisting(TaskRequestDto taskRequestDto) {
        taskRepository
                .findById(taskRequestDto.getId())
                .orElseThrow(() -> {
                    TaskNotFoundException exception =
                            new TaskNotFoundException(String.format("Задача с id #%d не существует. Изменение невозможно", taskRequestDto.getId()));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        projectRepository.findById(taskRequestDto.getProject().getId())
                .orElseThrow(() -> {
                    ProjectNotFoundException exception =
                            new ProjectNotFoundException(
                                    String.format("Проект с id #%d не существует. Изменение Задачи невозможно", taskRequestDto.getProject().getId()));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        releaseRepository
                .findById(taskRequestDto.getRelease().getId())
                .orElseThrow(() -> {
                    ReleaseNotFoundException exception =
                            new ReleaseNotFoundException(
                                    String.format("Релиз с id #%d не существует. Изменение Задачи невозможно", taskRequestDto.getRelease().getId())
                            );
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        userRepository
                .findById(taskRequestDto.getAuthor().getId())
                .orElseThrow(() -> {
                    UserNotFoundException exception =
                            new UserNotFoundException(
                                    String.format("Автор с id #%d не существует. Изменение Задачи невозможно", taskRequestDto.getAuthor().getId())
                            );
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        if (taskRequestDto.getExecutor() != null) {
            userRepository
                    .findById(taskRequestDto.getExecutor().getId())
                    .orElseThrow(() -> {
                        UserNotFoundException exception =
                                new UserNotFoundException(
                                        String.format(
                                                "Исполнитель с id #%d не существует. Изменение Задачи невозможно",
                                                taskRequestDto.getExecutor().getId()
                                        )
                                );
                        logger.error(exception.getMessage(), exception);
                        return exception;
                    });
        }
    }

    private Task convertFromRequestToEntity(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, Task.class);
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }


}
