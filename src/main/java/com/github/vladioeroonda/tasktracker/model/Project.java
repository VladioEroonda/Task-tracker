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
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "project", schema = "public")
public class Project implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;
    @Column(nullable = false)
    private BigDecimal price;
    @OneToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project", orphanRemoval = true)
    private List<Task> tasks;

    public Project() {
    }

    public Project(String name, ProjectStatus status, User customer, BigDecimal price) {
        this.name = name;
        this.status = status;
        this.customer = customer;
        this.price = price;
    }

    public Project(Long id, String name, ProjectStatus status, User customer, List<Task> tasks, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.customer = customer;
        this.tasks = tasks;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
        task.setProject(this);
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        if (this.tasks != null) {
            task.setProject(null);
            this.tasks.remove(task);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) && Objects.equals(name, project.name) && status == project.status && Objects.equals(price, project.price) && Objects.equals(customer, project.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, price, customer);
    }

    @Override
    public String toString() {
        return String.format("Project{ id= %d, name= %s, status= %s, customer= %s}", id, name, status, customer);
    }
}
