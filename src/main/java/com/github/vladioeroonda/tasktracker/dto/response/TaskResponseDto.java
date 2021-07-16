package com.github.vladioeroonda.tasktracker.dto.response;

import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность Задачи(ответ)")
public class TaskResponseDto {
    @Schema(description = "ID Задачи")
    private Long id;
    @Schema(description = "Тема Задачи")
    private String name;
    @Schema(description = "Описание Задачи")
    private String description;
    @Schema(description = "Статус Задачи")
    private TaskStatus status;
    @Schema(description = "Проект, к которому относится Задача")
    private ProjectResponseDto project;
    @Schema(description = "Информация по релизу Задачи")
    private ReleaseResponseDto release;
    @Schema(description = "Автор Задачи")
    private UserResponseDto author;
    @Schema(description = "Исполнитель Задачи")
    private UserResponseDto executor;

    public TaskResponseDto() {
    }

    public TaskResponseDto(Long id, String name, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public TaskResponseDto(
            Long id,
            String name,
            String description,
            TaskStatus status,
            ProjectResponseDto project,
            ReleaseResponseDto release,
            UserResponseDto author,
            UserResponseDto executor
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.project = project;
        this.release = release;
        this.author = author;
        this.executor = executor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public ProjectResponseDto getProject() {
        return project;
    }

    public void setProject(ProjectResponseDto project) {
        this.project = project;
    }

    public ReleaseResponseDto getRelease() {
        return release;
    }

    public void setRelease(ReleaseResponseDto release) {
        this.release = release;
    }

    public UserResponseDto getAuthor() {
        return author;
    }

    public void setAuthor(UserResponseDto author) {
        this.author = author;
    }

    public UserResponseDto getExecutor() {
        return executor;
    }

    public void setExecutor(UserResponseDto executor) {
        this.executor = executor;
    }
}
