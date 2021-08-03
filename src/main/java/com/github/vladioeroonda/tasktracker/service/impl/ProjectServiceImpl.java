package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectBadDataException;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectService;
import com.github.vladioeroonda.tasktracker.service.UserService;
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
    private final UserService userService;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(
            ProjectRepository projectRepository,
            UserService userService,
            ModelMapper modelMapper
    ) {
        this.projectRepository = projectRepository;
        this.userService = userService;
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
    public ProjectResponseDto getProjectByIdAndReturnResponseDto(Long id) {
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
    public Project getProjectByIdAndReturnEntity(Long id) {
        return projectRepository
                .findById(id)
                .orElseThrow(() -> {
                    ProjectNotFoundException exception =
                            new ProjectNotFoundException(String.format("Проект с id #%d не существует", id));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });
    }

    @Transactional
    @Override
    public void checkProjectExistsById(Long id) {
        if (projectRepository.findById(id).isEmpty()) {
            ProjectNotFoundException exception =
                    new ProjectNotFoundException(String.format("Проект с id #%d не существует", id));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @Transactional
    @Override
    public ProjectResponseDto addProject(ProjectRequestDto projectRequestDto) {
        logger.info("Добавление нового Проекта");

        Project projectForSave = convertFromRequestToEntity(projectRequestDto);
        projectForSave.setId(null);
        projectForSave.setStatus(ProjectStatus.IN_PROGRESS);

        User customer = userService.getUserByIdAndReturnEntity(projectRequestDto.getCustomer().getId());
        projectForSave.setCustomer(customer);

        Project savedProject = projectRepository.save(projectForSave);
        return convertFromEntityToResponse(savedProject);
    }

    @Transactional
    @Override
    public ProjectResponseDto updateProject(ProjectRequestDto projectRequestDto) {
        logger.info(String.format("Обновление Проекта с id #%d", projectRequestDto.getId()));

        if (projectRequestDto.getStatus() == ProjectStatus.FINISHED) {
            ProjectBadDataException exception =
                    new ProjectBadDataException("Недопустимый статус задачи");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

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

        User customer = userService.getUserByIdAndReturnEntity(projectRequestDto.getCustomer().getId());
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
