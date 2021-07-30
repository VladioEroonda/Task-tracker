package com.github.vladioeroonda.tasktracker.dto.request;

import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dto для изменения статуса проекта(запрос)")
public class ProjectStatusChangingRequestDto {
    @Schema(description = "Id Проекта")
    private Long id;
    @Schema(description = "Статус Проекта")
    private ProjectStatus projectStatus;

    public ProjectStatusChangingRequestDto() {
    }

    public ProjectStatusChangingRequestDto(Long id, ProjectStatus projectStatus) {
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
