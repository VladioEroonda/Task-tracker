package com.github.vladioeroonda.tasktracker.repository;

import com.github.vladioeroonda.tasktracker.model.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {
    @Query(value =
            "SELECT DISTINCT r FROM Task t " +
                    "JOIN Project p ON (t.project.id = p.id) " +
                    "JOIN Release r ON (t.release.id = r.id) " +
                    "WHERE p.id = :id AND r.finishTime IS NULL")
    List<Release> getAllNotClosedReleasesByProjectId(@Param("id") Long id);
}
