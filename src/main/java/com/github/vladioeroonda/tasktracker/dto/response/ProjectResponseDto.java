package com.github.vladioeroonda.tasktracker.dto.response;

import com.github.vladioeroonda.tasktracker.model.ProjectStatus;

public class ProjectResponseDto {
    private Long id;
    private String name;
    private ProjectStatus status;
    private UserResponseDto customer;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(
            Long id,
            String name,
            ProjectStatus status,
            UserResponseDto customer
    ) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.customer = customer;
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

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public UserResponseDto getCustomer() {
        return customer;
    }

    public void setCustomer(UserResponseDto customer) {
        this.customer = customer;
    }
}
