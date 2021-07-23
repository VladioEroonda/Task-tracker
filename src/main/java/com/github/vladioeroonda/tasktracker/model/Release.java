package com.github.vladioeroonda.tasktracker.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "release", schema = "public")
public class Release {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String version;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "finish_time", nullable = false)
    private LocalDateTime finishTime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "release", orphanRemoval = true)
    private List<Task> tasks;

    public Release() {
    }

    public Release(String version, LocalDateTime startTime, LocalDateTime finishTime) {
        this.version = version;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public Release(Long id, String version, LocalDateTime startTime, LocalDateTime finishTime, List<Task> tasks) {
        this.id = id;
        this.version = version;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        if (this.tasks == null) {
            this.tasks = new ArrayList<>();
        }
        task.setRelease(this);
        this.tasks.add(task);
    }

    public void removeTask(Task task) {
        if (this.tasks != null) {
            task.setRelease(null);
            this.tasks.remove(task);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Release release = (Release) o;
        return Objects.equals(id, release.id) &&
                Objects.equals(version, release.version) &&
                Objects.equals(startTime, release.startTime) &&
                Objects.equals(finishTime, release.finishTime) &&
                Objects.equals(tasks, release.tasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, startTime, finishTime, tasks);
    }
}
