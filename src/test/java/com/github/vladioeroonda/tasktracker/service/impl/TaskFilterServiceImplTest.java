package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.Release;
import com.github.vladioeroonda.tasktracker.model.Role;
import com.github.vladioeroonda.tasktracker.model.Task;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.TaskFilterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(profiles = "test")
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
class TaskFilterServiceImplTest {
    @Autowired
    private TaskFilterService taskFilterService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ReleaseRepository releaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtil testUtil;

    @Test
    void getFilteredTasks_ShouldReturnThreeTasks_WithSameDescription() {
        List<Task> expectedTasks = addThreeTasksWithSameDescription();

        List<TaskResponseDto> actualTasks = taskFilterService.getFilteredTasks(
                null,
                expectedTasks.get(0).getDescription(),
                null,
                null,
                null,
                null,
                null);

        assertEquals(expectedTasks.size(), actualTasks.size());
    }

    @Test
    void getFilteredTasks_ShouldReturnTwoOrMore_WhenTaskStatusInProgress() {
        List<Task> expectedTasks = addThreeTasksWithSameDescription();

        List<TaskResponseDto> actualTasks = taskFilterService.getFilteredTasks(
                null,
                null,
                TaskStatus.IN_PROGRESS,
                null,
                null,
                null,
                null);

        assertTrue(actualTasks.size() >= 2);
    }

    @Test
    void getFilteredTasks_ShouldReturnTasksByFewParams() {
        List<Task> expectedTasks = addThreeTasksWithSameDescription();
        String nameForSearch = expectedTasks.get(0).getName();
        String descriptionForSearch = expectedTasks.get(0).getDescription();
        TaskStatus statusForSearch = expectedTasks.get(0).getStatus();

        List<TaskResponseDto> actualTasks = taskFilterService.getFilteredTasks(
                nameForSearch,
                descriptionForSearch,
                statusForSearch,
                null,
                null,
                null,
                null);

        assertEquals(nameForSearch, actualTasks.get(0).getName());
        assertEquals(descriptionForSearch, actualTasks.get(0).getDescription());
        assertEquals(statusForSearch, expectedTasks.get(0).getStatus());
    }

    @Test
    void getFilteredTasks_ShouldReturnTasksByAllParams() {
        List<Task> expectedTasks = addThreeTasksWithSameDescription();
        String nameForSearch = expectedTasks.get(0).getName();
        String descriptionForSearch = expectedTasks.get(0).getDescription();
        TaskStatus statusForSearch = expectedTasks.get(0).getStatus();
        String projectNameForSearch = expectedTasks.get(0).getProject().getName();
        String releaseVersionForSearch = expectedTasks.get(0).getRelease().getVersion();
        String authorNameForSearch = expectedTasks.get(0).getAuthor().getName();
        String executorNameForSearch = null;
        if (expectedTasks.get(0).getExecutor() != null) {
            executorNameForSearch = expectedTasks.get(0).getExecutor().getName();
        }

        List<TaskResponseDto> actualTasks = taskFilterService.getFilteredTasks(
                nameForSearch,
                descriptionForSearch,
                statusForSearch,
                projectNameForSearch,
                releaseVersionForSearch,
                authorNameForSearch,
                executorNameForSearch);

        assertEquals(nameForSearch, actualTasks.get(0).getName());
        assertEquals(descriptionForSearch, actualTasks.get(0).getDescription());
        assertEquals(statusForSearch, expectedTasks.get(0).getStatus());
    }

    @AfterEach
    public void cleanUp() {
        testUtil.clearBase();
    }

    private List<Task> addThreeTasksWithSameDescription() {
        User user = new User(
                UUID.randomUUID().toString(),
                "testPassword",
                "TestName",
                "testBankAccountId",
                Set.of(Role.USER)
        );
        userRepository.save(user);

        Project project = new Project(
                "TestName",
                ProjectStatus.IN_PROGRESS,
                user,
                new BigDecimal("3000")
        );
        projectRepository.save(project);

        Release release = new Release(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                null
        );
        releaseRepository.save(release);

        String description = UUID.randomUUID().toString();
        Task taskForSave1 = new Task(null, "TestName1", description, TaskStatus.DONE, project, release, user, user);
        Task taskForSave2 = new Task("TestName2", description, TaskStatus.IN_PROGRESS, project, release, user);
        Task taskForSave3 = new Task("TestName3", description, TaskStatus.IN_PROGRESS, project, release, user);
        List<Task> tasks = new ArrayList<>();
        tasks.add(taskRepository.save(taskForSave1));
        tasks.add(taskRepository.save(taskForSave2));
        tasks.add(taskRepository.save(taskForSave3));
        return tasks;
    }
}
