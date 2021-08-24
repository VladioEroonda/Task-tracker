package com.github.vladioeroonda.tasktracker.Util;

import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

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

    public void deleteTestProjectAndUser(long projectId) {
        Optional<Project> projectFromBD = projectRepository.findById(projectId);
        if (projectFromBD.isPresent()) {
            projectRepository.delete(projectFromBD.get());
            Optional<User> userForDelete = userRepository.findById(projectFromBD.get().getCustomer().getId());
            userForDelete.ifPresent(user -> userRepository.delete(user));
        }
    }

    public void deleteTaskById(long taskId) {
        taskRepository.findById(taskId).ifPresent(
                (task -> {
                    projectRepository.delete(projectRepository.getById(task.getProject().getId()));
                    releaseRepository.delete(releaseRepository.getById(task.getRelease().getId()));
                    userRepository.delete(userRepository.getById(task.getAuthor().getId()));
                    taskRepository.delete(task);
                })
        );
    }
}
