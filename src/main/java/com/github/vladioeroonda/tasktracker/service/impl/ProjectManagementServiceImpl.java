package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectStatusChangingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectClosingException;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.Task;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectManagementService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectManagementServiceImpl implements ProjectManagementService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectManagementServiceImpl.class);

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public ProjectManagementServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public ProjectResponseDto closeProject(ProjectStatusChangingRequestDto projectRequestDto) {
        logger.info("Закрытие проекта");

        if (projectRequestDto.getProjectStatus() != ProjectStatus.FINISHED) {
            ProjectClosingException exception = new ProjectClosingException("Вы пытаетесь сменить статус проекта на отличный от FINISHED");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        Project projectFromBD = projectRepository
                .findById(projectRequestDto.getId())
                .orElseThrow(() -> {
                    ProjectNotFoundException exception = new ProjectNotFoundException(
                            String.format("Проект с id #%d не существует. Закрытие невозможно", projectRequestDto.getId()));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        if (projectFromBD.getStatus() == ProjectStatus.FINISHED) {
            ProjectClosingException exception = new ProjectClosingException("Данный проект уже закрыт!");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        List<Task> projectTasks =
                projectRepository.getAllNotClosedTasksByProjectId(projectFromBD.getId()) == null ?
                        new ArrayList<>() : projectRepository.getAllNotClosedTasksByProjectId(projectFromBD.getId());

        if (projectTasks.size() > 0) {
            ProjectClosingException exception = new ProjectClosingException(
                    String.format(
                            "Вы пытаетесь закрыть проект, в котором остались не закрытые задачи (%d)",
                            projectTasks.size()
                    ));
            logger.error(exception.getMessage(),exception);
            throw exception;
        }

        projectFromBD.setStatus(ProjectStatus.FINISHED);
        projectRepository.save(projectFromBD);

        return convertFromEntityToResponse(projectFromBD);
    }

    private ProjectResponseDto convertFromEntityToResponse(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }
}
