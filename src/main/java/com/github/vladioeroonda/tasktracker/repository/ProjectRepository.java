package com.github.vladioeroonda.tasktracker.repository;


import com.github.vladioeroonda.tasktracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

}
