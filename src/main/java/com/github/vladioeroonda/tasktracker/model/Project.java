package com.github.vladioeroonda.tasktracker.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
    private List<Task> tasks;

    public Project() {
    }

    public Project(String name, ProjectStatus status, User customer) {
        this.name = name;
        this.status = status;
        this.customer = customer;
    }

    public Project(Long id, String name, ProjectStatus status, User customer, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.customer = customer;
        this.tasks = tasks;
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

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}