package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            UserRepository userRepository,
            ModelMapper modelMapper
    ) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<ProjectResponseDto> getAllProjects() {
        logger.info("Получение списка всех Проектов");

        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public ProjectResponseDto getProjectById(Long id) {
        logger.info(String.format("Получение Проекта с id #%d", id));

        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> {
                    ProjectNotFoundException exception =
                            new ProjectNotFoundException(String.format("Проект с id #%d не существует", id));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        return convertFromEntityToResponse(project);
    }

    @Transactional
    @Override
    public ProjectResponseDto addProject(ProjectRequestDto projectRequestDto) {
        logger.info("Добавление нового Проекта");

        Project projectForSave = convertFromRequestToEntity(projectRequestDto);
        projectForSave.setId(null);
        projectForSave.setStatus(ProjectStatus.IN_PROGRESS);

        User customer = userRepository
                .findById(projectRequestDto.getCustomer().getId())
                .orElseThrow(() -> {
                    UserNotFoundException exception =
                            new UserNotFoundException(
                                    String.format(
                                            "Пользователь с id #%d не существует. Невозможно добавить Проект.",
                                            projectRequestDto.getCustomer().getId()
                                    )
                            );
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        projectForSave.setCustomer(customer);

        Project savedProject = projectRepository.save(projectForSave);
        return convertFromEntityToResponse(savedProject);
    }

    @Transactional
    @Override
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto) {
        logger.info(String.format("Обновление Проекта с id #%d", projectRequestDto.getId()));

        Project projectFromBD = projectRepository
                .findById(projectRequestDto.getId())
                .orElseThrow(() -> {
                    ProjectNotFoundException exception =
                            new ProjectNotFoundException(
                                    String.format("Проект с id #%d не существует. Обновление невозможно", projectRequestDto.getId())
                            );
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        Project projectForSave = convertFromRequestToEntity(projectRequestDto);
        projectForSave.setTasks(projectFromBD.getTasks());

        User customer = userRepository
                .findById(projectRequestDto.getCustomer().getId())
                .orElseThrow(() -> {
                    UserNotFoundException exception =
                            new UserNotFoundException(
                                    String.format(
                                            "Пользователь с id #%d не существует. Обновление Проекта невозможно",
                                            projectRequestDto.getCustomer().getId()
                                    )
                            );
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        projectForSave.setCustomer(customer);

        Project updatedProject = projectRepository.save(projectForSave);
        return convertFromEntityToResponse(updatedProject);
    }

    @Transactional
    @Override
    public void deleteProject(Long id) {
        logger.info(String.format("Удаление Проекта с id #%d", id));

        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> {
                    ProjectNotFoundException exception =
                            new ProjectNotFoundException(String.format("Проект с id #%d не существует. Удаление невозможно", id));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });
        projectRepository.delete(project);
    }

    private Project convertFromRequestToEntity(ProjectRequestDto requestDto) {
        return modelMapper.map(requestDto, Project.class);
    }

    private ProjectResponseDto convertFromEntityToResponse(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }
}
