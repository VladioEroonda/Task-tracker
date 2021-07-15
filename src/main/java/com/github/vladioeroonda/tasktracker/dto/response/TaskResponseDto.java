package com.github.vladioeroonda.tasktracker.dto.response;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;

public class TaskResponseDto {
    private Long id;
    private String name;
    private String description;
    private TaskStatus status;
    private ProjectResponseDto project;
    private ReleaseResponseDto release;
    private UserResponseDto author;
    private UserResponseDto executor;

    public TaskResponseDto() {
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
