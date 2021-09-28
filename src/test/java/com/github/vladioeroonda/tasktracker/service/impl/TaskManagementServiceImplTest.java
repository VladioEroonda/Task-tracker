package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
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
import com.github.vladioeroonda.tasktracker.service.TaskManagementService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles(profiles = "test")
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
class TaskManagementServiceImplTest {
    private static final int UNREACHABLE_ID = 100_000;
    @Autowired
    private TaskManagementService taskManagementService;
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
    void updateTask_ShouldBeSuccessful() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                existingTask.getName(),
                existingTask.getDescription(),
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId()),
                null
        );

        TaskResponseDto actual = taskManagementService.updateTask(expectedTask);

        assertNotNull(actual);
        assertEquals(expectedTask.getName(), actual.getName());
        assertEquals(expectedTask.getDescription(), actual.getDescription());
    }

    @Test
    void updateTask_ShouldThrowException_IfUpdatedNameLength_LessThanValueFromProperties() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                "throw",
                existingTask.getDescription(),
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId()),
                null
        );

        assertThrows(TaskBadDataException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @Test
    void updateTask_ShouldThrowException_IfUpdatedDescriptionLength_LessThanValueFromProperties() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                existingTask.getName(),
                "throw",
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId()),
                null
        );

        assertThrows(TaskBadDataException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @Test
    void updateTask_ShouldThrowException_WhenStatusBackLog_AndExecutorNotSet() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                existingTask.getName(),
                "throw",
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId()),
                null
        );

        assertThrows(TaskBadDataException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @Test
    void updateTask_ShouldThrowException_WhenStatusBackLog_AndExecutorNotExists() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                existingTask.getName(),
                "throw",
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId()),
                new UserRequestDto(existingTask.getAuthor().getId() + UNREACHABLE_ID)
        );

        assertThrows(UserNotFoundException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @Test
    void updateTask_ShouldThrowException_WhenUpdatedTaskNotExists() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId() + UNREACHABLE_ID,
                existingTask.getName(),
                existingTask.getDescription(),
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId()),
                null
        );

        assertThrows(TaskNotFoundException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @Test
    void updateTask_ShouldThrowException_WhenUpdatedProjectNotExists() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                existingTask.getName(),
                existingTask.getDescription(),
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId() + UNREACHABLE_ID),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId()),
                null
        );

        assertThrows(ProjectNotFoundException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @Test
    void updateTask_ShouldThrowException_WhenUpdatedReleaseNotExists() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                existingTask.getName(),
                existingTask.getDescription(),
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId() + UNREACHABLE_ID),
                new UserRequestDto(existingTask.getAuthor().getId()),
                null
        );

        assertThrows(ReleaseNotFoundException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @Test
    void updateTask_ShouldThrowException_WhenUpdatedAuthorNotExists() {
        Task existingTask = returnFormedTask();
        TaskRequestDto expectedTask = new TaskRequestDto(
                existingTask.getId(),
                existingTask.getName(),
                existingTask.getDescription(),
                TaskStatus.BACKLOG,
                new ProjectRequestDto(existingTask.getProject().getId()),
                new ReleaseRequestDto(existingTask.getRelease().getId()),
                new UserRequestDto(existingTask.getAuthor().getId() + UNREACHABLE_ID),
                null
        );

        assertThrows(UserNotFoundException.class, () -> {
            taskManagementService.updateTask(expectedTask);
        });
    }

    @AfterEach
    public void cleanUp() {
        testUtil.clearBase();
    }

    private Task returnFormedTask() {
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

        Task taskForSave1 = new Task(null, "testTaskName", "description", TaskStatus.IN_PROGRESS, project, release, user, user);

        return taskRepository.save(taskForSave1);
    }
}