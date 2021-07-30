package com.github.vladioeroonda.tasktracker.repository;


import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EnumType;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT t FROM Project p JOIN Task t ON (p.id = t.project.id ) WHERE NOT t.status = 'FINISHED'")
    List<Task> getAllNotClosedTasks();

}
