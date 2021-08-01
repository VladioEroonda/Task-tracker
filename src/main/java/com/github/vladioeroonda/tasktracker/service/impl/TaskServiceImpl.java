package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.Release;
import com.github.vladioeroonda.tasktracker.model.Task;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.TaskService;
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
    private final ProjectRepository projectRepository;
    private final ReleaseRepository releaseRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public TaskServiceImpl(
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
    public TaskResponseDto getTaskById(Long id) {
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
    public TaskResponseDto addTask(TaskRequestDto taskRequestDto) {
        logger.info("Добавление Задачи");

        Task taskForSave = convertFromRequestToEntity(taskRequestDto);
        taskForSave.setId(null);
        taskForSave.setStatus(TaskStatus.BACKLOG);

        if (taskRequestDto.getName().length() < TASK_NAME_MIN_LENGTH) {
            TaskBadDataException exception =
                    new TaskBadDataException(String.format("Слишком короткое имя задачи. Должно быть длиннее %d символов", TASK_NAME_MIN_LENGTH));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        if (taskRequestDto.getDescription().length() < TASK_DESCRIPTION_MIN_LENGTH) {
            TaskBadDataException exception =
                    new TaskBadDataException(
                            String.format("Слишком короткое описание задачи. Должно быть длиннее %d символов", TASK_DESCRIPTION_MIN_LENGTH)
                    );
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        Project project = projectRepository.findById(taskRequestDto.getProject().getId())
                .orElseThrow(() -> {
                    ProjectNotFoundException exception =
                            new ProjectNotFoundException(
                                    String.format("Проект с id #%d не существует. Невозможно добавить Задачу", taskRequestDto.getProject().getId())
                            );
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });

        if (project.getStatus() == ProjectStatus.FINISHED) {
            TaskBadDataException exception =
                    new TaskBadDataException("Вы пытаетесь добавить задачу в уже закрытый проект");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
        taskForSave.setProject(project);

        Release release = releaseRepository
                .findById(taskRequestDto.getRelease().getId())
                .orElseThrow(() -> {
                    ReleaseNotFoundException exception =
                            new ReleaseNotFoundException(
                                    String.format("Релиз с id #%d не существует. Невозможно добавить Задачу", taskRequestDto.getRelease().getId())
                            );
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });
        taskForSave.setRelease(release);

        User author = userRepository
                .findById(taskRequestDto.getAuthor().getId())
                .orElseThrow(() -> {
                    UserNotFoundException exception =
                            new UserNotFoundException(
                                    String.format("Автор с id #%d не существует. Невозможно добавить Задачу", taskRequestDto.getAuthor().getId())
                            );
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });
        taskForSave.setAuthor(author);

        if (taskRequestDto.getExecutor() != null) {
            User executor = userRepository
                    .findById(taskRequestDto.getExecutor().getId())
                    .orElseThrow(() -> {
                        UserNotFoundException exception =
                                new UserNotFoundException(
                                        String.format(
                                                "Исполнитель с id #%d не существует. Невозможно добавить Задачу",
                                                taskRequestDto.getExecutor().getId()
                                        )
                                );
                        logger.error(exception.getMessage(), exception);
                        throw exception;
                    });
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

    private Task convertFromRequestToEntity(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, Task.class);
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }
}
