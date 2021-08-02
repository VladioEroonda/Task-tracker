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
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {
    private static final Logger logger = LoggerFactory.getLogger(TaskManagementServiceImpl.class);

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ReleaseService releaseService;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public TaskManagementServiceImpl(
            TaskRepository taskRepository,
            UserService userService,
            ReleaseService releaseService,
            ProjectService projectService,
            ModelMapper modelMapper
    ) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.releaseService = releaseService;
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public TaskResponseDto closeTask(TaskRequestDto taskRequestDto) {
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

        taskRepository.existsById(taskRequestDto.getId());
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
