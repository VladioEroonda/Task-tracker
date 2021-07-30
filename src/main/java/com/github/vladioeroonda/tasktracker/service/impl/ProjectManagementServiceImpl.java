package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectStatusChangingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectClosingException;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectManagementService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectManagementServiceImpl implements ProjectManagementService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    public ProjectManagementServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public ProjectResponseDto closeProject(ProjectStatusChangingRequestDto projectRequestDto) {

        if (projectRequestDto.getProjectStatus() != ProjectStatus.FINISHED) {
            throw new ProjectClosingException("Вы пытаетесь сменить статус проекта на отличный от FINISHED");
        }

        Project projectFromBD = projectRepository
                .findById(projectRequestDto.getId())
                .orElseThrow(
                        () -> new ProjectNotFoundException(
                                String.format("Проект с id #%d не существует", projectRequestDto.getId()))
                );

        if (projectFromBD.getStatus() == ProjectStatus.FINISHED) {
            throw new ProjectClosingException("Данный проект уже закрыт!");
        }


        if (projectRepository.getAllNotClosedTasks() != null && projectRepository.getAllNotClosedTasks().size()>0) {
            throw new ProjectClosingException("Вы пытаетесь закрыть проект, в котором остались не закрытые задачи");
        }

        projectFromBD.setStatus(ProjectStatus.FINISHED);
        projectRepository.save(projectFromBD);
        ProjectResponseDto projectForReturn = convertFromEntityToResponse(projectFromBD);

        return projectForReturn;
    }

    private ProjectResponseDto convertFromEntityToResponse(Project project) {
        return modelMapper.map(project, ProjectResponseDto.class);
    }
}
