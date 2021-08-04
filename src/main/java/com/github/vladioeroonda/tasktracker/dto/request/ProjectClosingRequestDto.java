package com.github.vladioeroonda.tasktracker.dto.request;

import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto для закрытия Проекта(запрос)")
public class ProjectClosingRequestDto {
    @Schema(description = "Id Проекта")
    private Long id;
    @Schema(description = "Статус Проекта")
    private ProjectStatus projectStatus;

    public ProjectClosingRequestDto() {
    }

    public ProjectClosingRequestDto(Long id, ProjectStatus projectStatus) {
        this.id = id;
        this.projectStatus = projectStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }
}
