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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskManagementServiceImpl implements TaskManagementService {

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

        fieldsCheckForExisting(taskRequestDto);

        if (!taskRequestDto.getStatus().equals(TaskStatus.BACKLOG) && taskRequestDto.getExecutor() == null) {
            throw new TaskBadDataException(
                    String.format("Задаче id #%d не назначен Исполнитель", taskRequestDto.getId())
            );
        }

        Task taskForSave = convertFromRequestToEntity(taskRequestDto);
        Task updatedTask = taskRepository.save(taskForSave);
        return convertFromEntityToResponse(updatedTask);
    }

    private void fieldsCheckForExisting(TaskRequestDto taskRequestDto) {
        taskRepository
                .findById(taskRequestDto.getId())
                .orElseThrow(
                        () -> new TaskNotFoundException(
                                String.format("Задача с id #%d не существует", taskRequestDto.getId())
                        )
                );

        projectRepository.findById(taskRequestDto.getProject().getId())
                .orElseThrow(
                        () -> new ProjectNotFoundException(
                                String.format("Проект с id #%d не существует", taskRequestDto.getProject().getId()))
                );

        releaseRepository
                .findById(taskRequestDto.getRelease().getId())
                .orElseThrow(
                        () -> new ReleaseNotFoundException(
                                String.format("Релиз с id #%d не существует", taskRequestDto.getRelease().getId()))
                );

        userRepository
                .findById(taskRequestDto.getAuthor().getId())
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Автор с id #%d не существует", taskRequestDto.getAuthor().getId()))
                );

        if (taskRequestDto.getExecutor() != null) {
            userRepository
                    .findById(taskRequestDto.getExecutor().getId())
                    .orElseThrow(
                            () -> new UserNotFoundException(
                                    String.format("Исполнитель с id #%d не существует", taskRequestDto.getExecutor().getId()))
                    );
        }
    }

    private Task convertFromRequestToEntity(TaskRequestDto requestDto) {
        return modelMapper.map(requestDto, Task.class);
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }


}
