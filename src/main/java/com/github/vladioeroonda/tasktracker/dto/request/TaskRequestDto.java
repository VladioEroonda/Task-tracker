package com.github.vladioeroonda.tasktracker.dto.request;

import com.github.vladioeroonda.tasktracker.model.TaskStatus;

public class TaskRequestDto {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private ProjectRequestDto project;
    private ReleaseRequestDto release;
    private UserRequestDto author;
    private UserRequestDto executor;

    public TaskRequestDto() {
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
