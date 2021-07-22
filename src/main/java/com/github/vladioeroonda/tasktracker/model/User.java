package com.github.vladioeroonda.tasktracker.model;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String name;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", orphanRemoval = true)
    private List<Task> tasksAsAuthor;
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "executor")
    private List<Task> tasksAsExecutor;

    public User() {
    }

    public User(String login, String name, Set<Role> roles) {
        this.login = login;
        this.name = name;
        this.roles = roles;
    }

    public User(Long id, String login, String name, Set<Role> roles, List<Task> tasksAsAuthor, List<Task> tasksAsExecutor) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.roles = roles;
        this.tasksAsAuthor = tasksAsAuthor;
        this.tasksAsExecutor = tasksAsExecutor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Task> getTasksAsAuthor() {
        return tasksAsAuthor;
    }

    public void setTasksAsAuthor(List<Task> tasksAsAuthor) {
        this.tasksAsAuthor = tasksAsAuthor;
    }

    public List<Task> getTasksAsExecutor() {
        return tasksAsExecutor;
    }

    public void setTasksAsExecutor(List<Task> tasksAsExecutor) {
        this.tasksAsExecutor = tasksAsExecutor;
    }
}
