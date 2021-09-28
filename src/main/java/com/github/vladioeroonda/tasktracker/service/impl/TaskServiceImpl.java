package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.CSVParsingException;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.WrongFileTypeException;
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
import com.github.vladioeroonda.tasktracker.util.Translator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Value("${task.min-length.name}")
    private int minNameLength;
    @Value("${task.min-length.description}")
    private int minDescriptionLength;

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
                            new TaskNotFoundException(String.format(Translator.toLocale("exception.task.not-found-by-id"), id));
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
                            new TaskNotFoundException(String.format(Translator.toLocale("exception.task.not-found-by-id"), id));
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
                    = new TaskNotFoundException(String.format(Translator.toLocale("exception.task.not-found-by-id"), id));
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

        if (taskRequestDto.getName().length() < minNameLength) {
            TaskBadDataException exception =
                    new TaskBadDataException(
                            String.format(Translator.toLocale("exception.task.too-short-task-name"),
                                    taskRequestDto.getName(),
                                    minNameLength)
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

        Project project =
                projectService.getProjectByIdAndReturnEntity(taskRequestDto.getProject().getId());

        if (project.getStatus() == ProjectStatus.FINISHED) {
            TaskBadDataException exception =
                    new TaskBadDataException(Translator.toLocale("exception.task.cant-add-task-to-closed-project"));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
        taskForSave.setProject(project);

        Release release =
                releaseService.getReleaseByIdAndReturnEntity(taskRequestDto.getRelease().getId());

        if (release.getFinishTime() != null) {
            TaskBadDataException exception =
                    new TaskBadDataException(Translator.toLocale("exception.task.cant-add-task-to-closed-release"));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
        taskForSave.setRelease(release);

        User author =
                userService.getUserByIdAndReturnEntity(taskRequestDto.getAuthor().getId());
        taskForSave.setAuthor(author);

        if (taskRequestDto.getExecutor() != null) {
            User executor =
                    userService.getUserByIdAndReturnEntity(taskRequestDto.getExecutor().getId());
            taskForSave.setExecutor(executor);
        }

        Task savedTask = taskRepository.save(taskForSave);
        return convertFromEntityToResponse(savedTask);
    }

    @Override
    public List<TaskResponseDto> addTaskByCsv(MultipartFile file) {
        logger.info("Добавление Задачи с помощью CSV-файла");

        logger.debug(String.format("Проверка типа файла: %s", file.getContentType()));

        if (!file.getContentType().equals("text/csv")) {
            WrongFileTypeException exception =
                    new WrongFileTypeException(Translator.toLocale("exception.task.csv.wrong-format"));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        List<TaskResponseDto> addedTasks = new ArrayList<>();
        try (
                BufferedReader fileReader =
                        new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
                CSVParser parser =
                        new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim())//.withIgnoreHeaderCase()
        ) {

            Iterable<CSVRecord> csvRecords = parser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                TaskRequestDto taskForSave = new TaskRequestDto();
                taskForSave.setName(csvRecord.get("Name"));
                taskForSave.setDescription(csvRecord.get("Description"));
                taskForSave.setProject(new ProjectRequestDto(Long.parseLong(csvRecord.get("ProjectId"))));
                taskForSave.setRelease(new ReleaseRequestDto(Long.parseLong(csvRecord.get("ReleaseId"))));
                taskForSave.setAuthor(new UserRequestDto(Long.parseLong(csvRecord.get("AuthorId"))));
                if (!csvRecord.get("ExecutorId").isEmpty() && !csvRecord.get("ExecutorId").isBlank()) {
                    taskForSave.setExecutor(new UserRequestDto(Long.parseLong(csvRecord.get("ExecutorId"))));
                }
                addedTasks.add(addTask(taskForSave));
            }

        } catch (IOException e) {
            CSVParsingException exception
                    = new CSVParsingException(Translator.toLocale("exception.task.csv.parse-error"), e);
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        return addedTasks;
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        logger.info(String.format("Удаление Задачи с id #%d", id));

        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> {
                    TaskNotFoundException exception =
                            new TaskNotFoundException(String.format(Translator.toLocale("exception.task.not-found-by-id"), id));
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });

        taskRepository.delete(task);
    }

    @Transactional
    @Override
    public int countUnfinishedTasksByReleaseId(Long id) {
        releaseService.checkReleaseExistsById(id);
        return taskRepository.countUnfinishedTasksByReleaseId(id);
    }

    @Transactional
    @Override
    public void setAllTasksCancelled(Long releaseId) {
        releaseService.checkReleaseExistsById(releaseId);
        taskRepository.setAllTasksCancelled(releaseId);
    }

    private Task convertFromRequestToEntity(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, Task.class);
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }
}
