package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.model.Task;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectService;
import com.github.vladioeroonda.tasktracker.service.ReleaseService;
import com.github.vladioeroonda.tasktracker.service.TaskManagementService;
import com.github.vladioeroonda.tasktracker.service.TaskService;
import com.github.vladioeroonda.tasktracker.service.UserService;
import com.github.vladioeroonda.tasktracker.util.Translator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagementServiceImpl.class);

    @Value("${task.min-length.name}")
    private int minNameLength;
    @Value("${task.min-length.description}")
    private int minDescriptionLength;

    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ReleaseService releaseService;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public TaskManagementServiceImpl(
            TaskService taskService,
            TaskRepository taskRepository,
            UserService userService,
            ReleaseService releaseService,
            ProjectService projectService,
            ModelMapper modelMapper
    ) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.releaseService = releaseService;
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public TaskResponseDto updateTask(TaskRequestDto taskRequestDto) {
        logger.info("Выполнение Задачи (изменение полей)");

        fieldsCheckForExisting(taskRequestDto);

        if (taskRequestDto.getName().length() < minNameLength) {
            TaskBadDataException exception =
                    new TaskBadDataException(
                            String.format(Translator.toLocale("exception.task.too-short-task-name"), taskRequestDto.getName(), minNameLength)
                    );
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        if (taskRequestDto.getDescription().length() < minDescriptionLength) {
            TaskBadDataException exception =
                    new TaskBadDataException(
                            String.format(Translator.toLocale("exception.task.too-short-task-description"), minDescriptionLength)
                    );
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        if (!taskRequestDto.getStatus().equals(TaskStatus.BACKLOG) && taskRequestDto.getExecutor() == null) {
            TaskBadDataException exception =
                    new TaskBadDataException(String.format(
                            Translator.toLocale("exception.task-management.task-with-id-has-no-executor"), taskRequestDto.getId(), TaskStatus.BACKLOG)
                    );
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        Task taskForSave = convertFromRequestToEntity(taskRequestDto);
        Task updatedTask = taskRepository.save(taskForSave);
        return convertFromEntityToResponse(updatedTask);
    }

    private void fieldsCheckForExisting(TaskRequestDto taskRequestDto) {

        taskService.checkTaskExistsById(taskRequestDto.getId());
        projectService.checkProjectExistsById(taskRequestDto.getProject().getId());
        releaseService.checkReleaseExistsById(taskRequestDto.getRelease().getId());
        userService.checkUserExistsById(taskRequestDto.getAuthor().getId());

        if (taskRequestDto.getExecutor() != null) {
            userService.checkUserExistsById(taskRequestDto.getExecutor().getId());
        }
    }

    private Task convertFromRequestToEntity(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, Task.class);
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }
}
