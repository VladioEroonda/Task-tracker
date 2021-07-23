package com.github.vladioeroonda.tasktracker.repository;

import com.github.vladioeroonda.tasktracker.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

}
