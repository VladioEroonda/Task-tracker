package com.github.vladioeroonda.tasktracker.Util;

import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestUtil {
    private UserRepository userRepository;
    private ProjectRepository projectRepository;
    private ReleaseRepository releaseRepository;
    private TaskRepository taskRepository;

    public TestUtil(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            ReleaseRepository releaseRepository,
            TaskRepository taskRepository
    ) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.releaseRepository = releaseRepository;
        this.taskRepository = taskRepository;
    }

    @Bean
    public TestUtil testUtils(
            UserRepository userRepository,
            ProjectRepository projectRepository,
            ReleaseRepository releaseRepository,
            TaskRepository taskRepository) {
        return new TestUtil(userRepository, projectRepository, releaseRepository, taskRepository);
    }

    public void clearBase() {
        taskRepository.deleteAllInBatch();
        projectRepository.deleteAllInBatch();
        releaseRepository.deleteAllInBatch();
        userRepository.deleteAll();
    }
}
