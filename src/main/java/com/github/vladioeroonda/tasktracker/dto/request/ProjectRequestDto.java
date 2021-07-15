package com.github.vladioeroonda.tasktracker.dto.request;

import com.github.vladioeroonda.tasktracker.model.ProjectStatus;

public class ProjectRequestDto {
    private Long id;
    private String name;
    private ProjectStatus status;
    private UserRequestDto customer;

    public ProjectRequestDto() {
    }

    public ProjectRequestDto(
            Long id,
            String name,
            ProjectStatus status,
            UserRequestDto customer
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

    public UserRequestDto getCustomer() {
        return customer;
    }

    public void setCustomer(UserRequestDto customer) {
        this.customer = customer;
    }
}
