package com.github.vladioeroonda.tasktracker.repository;

import com.github.vladioeroonda.tasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Query(value =
            "SELECT count(t) FROM Task t " +
                    "JOIN Release r ON (t.release.id = r.id ) " +
                    "WHERE r.id= :id " +
                    "AND t.status NOT IN(com.github.vladioeroonda.tasktracker.model.TaskStatus.DONE, " +
                    "com.github.vladioeroonda.tasktracker.model.TaskStatus.CANCELLED )")
    int countUnfinishedTasksByReleaseId(@Param("id") Long id);

    @Modifying
    @Query(value =
            "UPDATE Task t " +
                    "SET t.status = com.github.vladioeroonda.tasktracker.model.TaskStatus.CANCELLED " +
                    "WHERE t.release.id = :id " +
                    "AND NOT t.status = com.github.vladioeroonda.tasktracker.model.TaskStatus.DONE")
    void setAllTasksCancelled(@Param("id") Long id);
}
