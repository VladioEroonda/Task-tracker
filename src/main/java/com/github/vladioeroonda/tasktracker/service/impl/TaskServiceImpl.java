package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;
    private final ReleaseRepository releaseRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ModelMapper modelMapper,
            ProjectRepository projectRepository,
            ReleaseRepository releaseRepository,
            UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
        this.projectRepository = projectRepository;
        this.releaseRepository = releaseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public List<TaskResponseDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(
                        () -> new TaskNotFoundException(String.format("Задача с id #%s не существует", id))
                );

        return convertFromEntityToResponse(task);
    }

    @Transactional
    @Override
    public TaskResponseDto addTask(TaskRequestDto taskRequestDto) {
        Task taskForSave = convertFromRequestToEntity(taskRequestDto);
        taskForSave.setId(null);
        taskForSave.setStatus(TaskStatus.BACKLOG);

        Project project = projectRepository.findById(taskRequestDto.getProject().getId())
                .orElseThrow(
                        () -> new ProjectNotFoundException(
                                String.format("Проект с id #%s не существует", taskRequestDto.getProject().getId()))
                );
        taskForSave.setProject(project);

        Release release = releaseRepository
                .findById(taskRequestDto.getRelease().getId())
                .orElseThrow(
                        () -> new ReleaseNotFoundException(
                                String.format("Релиз с id #%s не существует", taskRequestDto.getRelease().getId()))
                );
        taskForSave.setRelease(release);

        User author = userRepository
                .findById(taskRequestDto.getAuthor().getId())
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Автор с id #%s не существует", taskRequestDto.getAuthor().getId()))
                );
        taskForSave.setAuthor(author);

        if (taskRequestDto.getExecutor() != null) {
            User executor = userRepository
                    .findById(taskRequestDto.getExecutor().getId())
                    .orElseThrow(
                            () -> new UserNotFoundException(
                                    String.format("Исполнитель с id #%s не существует", taskRequestDto.getExecutor().getId()))
                    );

            taskForSave.setExecutor(executor);
        }

        Task savedTask = taskRepository.save(taskForSave);
        return convertFromEntityToResponse(savedTask);
    }

    @Transactional
    @Override
    public TaskResponseDto updateTask(TaskRequestDto taskRequestDto) {
        taskRepository
                .findById(taskRequestDto.getId())
                .orElseThrow(
                        () -> new TaskNotFoundException(
                                String.format("Задача с id #%s не существует", taskRequestDto.getId())
                        )
                );

        projectRepository.findById(taskRequestDto.getProject().getId())
                .orElseThrow(
                        () -> new ProjectNotFoundException(
                                String.format("Проект с id #%s не существует", taskRequestDto.getProject().getId()))
                );

        releaseRepository
                .findById(taskRequestDto.getRelease().getId())
                .orElseThrow(
                        () -> new ReleaseNotFoundException(
                                String.format("Релиз с id #%s не существует", taskRequestDto.getRelease().getId()))
                );

        userRepository
                .findById(taskRequestDto.getAuthor().getId())
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Автор с id #%s не существует", taskRequestDto.getAuthor().getId()))
                );

        if (taskRequestDto.getExecutor() != null) {
            userRepository
                    .findById(taskRequestDto.getExecutor().getId())
                    .orElseThrow(
                            () -> new UserNotFoundException(
                                    String.format("Исполнитель с id #%s не существует", taskRequestDto.getExecutor().getId()))
                    );
        }

        Task taskForSave = convertFromRequestToEntity(taskRequestDto);

        Task updatedTask = taskRepository.save(taskForSave);
        return convertFromEntityToResponse(updatedTask);
    }

    @Transactional
    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(
                        () -> new TaskNotFoundException(String.format("Задача с id #%s не существует", id))
                );

        taskRepository.delete(task);
    }

    private Task convertFromRequestToEntity(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, Task.class);
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }
}
