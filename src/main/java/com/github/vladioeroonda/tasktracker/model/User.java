package com.github.vladioeroonda.tasktracker.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user", schema = "public")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(name = "bank_account_id")
    private String bankAccountId;
    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", schema = "public", joinColumns = @JoinColumn(name = "user_id"))
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

    public User(String login, String password, String name, String bankAccountId, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.bankAccountId = bankAccountId;
        this.roles = roles;
    }

    public User(
            Long id,
            String login,
            String password,
            String name,
            String bankAccountId,
            Set<Role> roles,
            List<Task> tasksAsAuthor,
            List<Task> tasksAsExecutor
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.bankAccountId = bankAccountId;
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

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void addAuthorTask(Task task) {
        if (this.tasksAsAuthor == null) {
            this.tasksAsAuthor = new ArrayList<>();
        }
        task.setAuthor(this);
        this.tasksAsAuthor.add(task);
    }

    public void removeAuthorTask(Task task) {
        if (this.tasksAsAuthor != null) {
            task.setAuthor(null);
            this.tasksAsAuthor.remove(task);
        }
    }

    public void addExecutorTask(Task task) {
        if (this.tasksAsExecutor == null) {
            this.tasksAsExecutor = new ArrayList<>();
        }
        task.setExecutor(this);
        this.tasksAsExecutor.add(task);
    }

    public void removeExecutorTask(Task task) {
        if (this.tasksAsExecutor != null) {
            task.setExecutor(null);
            this.tasksAsExecutor.remove(task);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(bankAccountId, user.bankAccountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, name, bankAccountId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return String.format("User{ id= %d, login= %s, password= %s, name= %s, roles= %s }", id, login, password, name, roles);
    }
}
