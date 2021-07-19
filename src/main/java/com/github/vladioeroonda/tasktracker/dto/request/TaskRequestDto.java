package com.github.vladioeroonda.tasktracker.dto.request;

import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Сущность Задачи(запрос)")
public class TaskRequestDto {
    @Schema(description = "ID Задачи")
    private Long id;
    @Schema(description = "Тема Задачи")
    private String name;
    @Schema(description = "Описание Задачи")
    private String description;
    @Schema(description = "Статус Задачи")
    private TaskStatus status;
    @Schema(description = "Проект, к которому относится Задача")
    private ProjectRequestDto project;
    @Schema(description = "Информация по релизу Задачи")
    private ReleaseRequestDto release;
    @Schema(description = "Автор Задачи")
    private UserRequestDto author;
    @Schema(description = "Исполнитель Задачи")
    private UserRequestDto executor;

    public TaskRequestDto() {
    }

    public TaskRequestDto(Long id, String name, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public TaskRequestDto(
            Long id,
            String name,
            String description,
            TaskStatus status,
            ProjectRequestDto project,
            ReleaseRequestDto release,
            UserRequestDto author,
            UserRequestDto executor
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

    public ProjectRequestDto getProject() {
        return project;
    }

    public void setProject(ProjectRequestDto project) {
        this.project = project;
    }

    public ReleaseRequestDto getRelease() {
        return release;
    }

    public void setRelease(ReleaseRequestDto release) {
        this.release = release;
    }

    public UserRequestDto getAuthor() {
        return author;
    }

    public void setAuthor(UserRequestDto author) {
        this.author = author;
    }

    public UserRequestDto getExecutor() {
        return executor;
    }

    public void setExecutor(UserRequestDto executor) {
        this.executor = executor;
    }
}
