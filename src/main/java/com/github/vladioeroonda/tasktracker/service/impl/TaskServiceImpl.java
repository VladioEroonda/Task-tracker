package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.Release;
import com.github.vladioeroonda.tasktracker.model.Task;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectService;
import com.github.vladioeroonda.tasktracker.service.ReleaseService;
import com.github.vladioeroonda.tasktracker.service.TaskService;
import com.github.vladioeroonda.tasktracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private static final int TASK_NAME_MIN_LENGTH = 5;
    private static final int TASK_DESCRIPTION_MIN_LENGTH = 20;

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final ReleaseService releaseService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ProjectService projectService,
            ReleaseService releaseService,
            UserService userService,
            ModelMapper modelMapper
    ) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.releaseService = releaseService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<TaskResponseDto> getAllTasks() {
        logger.info("Получение списка Задач");

        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public TaskResponseDto getTaskByIdAndReturnResponseDto(Long id) {
        logger.info(String.format("Получение Задачи с id #%d", id));

        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> {
                    TaskNotFoundException exception =
                            new TaskNotFoundException(String.format("Задача с id #%s не существует", id));
                    logger.debug(exception.getMessage(), exception);
                    return exception;
                });

        return convertFromEntityToResponse(task);
    }

    @Transactional
    @Override
    public Task getTaskByIdAndReturnEntity(Long id) {
        logger.info(String.format("Получение Задачи с id #%d", id));

        return taskRepository
                .findById(id)
                .orElseThrow(() -> {
                    TaskNotFoundException exception =
                            new TaskNotFoundException(String.format("Задача с id #%s не существует", id));
                    logger.debug(exception.getMessage(), exception);
                    return exception;
                });
    }

    @Transactional
    @Override
    public void checkTaskExistsById(Long id) {
        logger.info(String.format("Проверка существования Задачи с id #%d", id));

        if (taskRepository.findById(id).isEmpty()) {
            TaskNotFoundException exception
                    = new TaskNotFoundException(String.format("Задача с id #%d не существует.", id));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @Transactional
    @Override
    public TaskResponseDto addTask(TaskRequestDto taskRequestDto) {
        logger.info("Добавление Задачи");

        Task taskForSave = convertFromRequestToEntity(taskRequestDto);
        taskForSave.setId(null);
        taskForSave.setStatus(TaskStatus.BACKLOG);

        if (taskRequestDto.getName().length() < TASK_NAME_MIN_LENGTH) {
            TaskBadDataException exception =
                    new TaskBadDataException(String.format("Слишком короткое имя Задачи. Должно быть длиннее %d символов", TASK_NAME_MIN_LENGTH));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        if (taskRequestDto.getDescription().length() < TASK_DESCRIPTION_MIN_LENGTH) {
            TaskBadDataException exception =
                    new TaskBadDataException(
                            String.format("Слишком короткое описание Задачи. Должно быть длиннее %d символов", TASK_DESCRIPTION_MIN_LENGTH)
                    );
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        Project project = projectService.getProjectByIdAndReturnEntity(taskRequestDto.getProject().getId());

        if (project.getStatus() == ProjectStatus.FINISHED) {
            TaskBadDataException exception =
                    new TaskBadDataException("Вы пытаетесь добавить задачу в уже закрытый проект");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
        taskForSave.setProject(project);

        Release release = releaseService.getReleaseByIdAndReturnEntity(taskRequestDto.getRelease().getId());
        taskForSave.setRelease(release);

        User author = userService.getUserByIdAndReturnEntity(taskRequestDto.getAuthor().getId());
        taskForSave.setAuthor(author);

        if (taskRequestDto.getExecutor() != null) {
            User executor = userService.getUserByIdAndReturnEntity(taskRequestDto.getExecutor().getId());
            taskForSave.setExecutor(executor);
        }

        Task savedTask = taskRepository.save(taskForSave);
        return convertFromEntityToResponse(savedTask);
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        logger.info(String.format("Удаление Задачи с id #%d", id));

        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> {
                    TaskNotFoundException exception =
                            new TaskNotFoundException(String.format("Задача с id #%d не существует. Удаление невозможно", id));
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });

        taskRepository.delete(task);
    }

    @Transactional
    @Override
    public int countUnfinishedTasksByReleaseId(Long id) {
        return taskRepository.countUnfinishedTasksByReleaseId(id);
    }

    @Transactional
    @Override
    public void setAllTasksCancelled(Long id) {
        taskRepository.setAllTasksCancelled(id);
    }

    private Task convertFromRequestToEntity(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, Task.class);
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }
}
