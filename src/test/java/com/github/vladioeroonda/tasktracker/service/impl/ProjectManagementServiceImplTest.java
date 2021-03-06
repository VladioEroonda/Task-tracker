package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.request.ProjectClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectClosingException;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
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
import com.github.vladioeroonda.tasktracker.service.ProjectManagementService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;


@ActiveProfiles(profiles = "test")
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
class ProjectManagementServiceImplTest {
    private static final int UNREACHABLE_ID = 100_000;
    @Autowired
    private ProjectManagementService projectManagementService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ReleaseRepository releaseRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TestUtil testUtil;

    @Test
    void closeProject() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.IN_PROGRESS), ProjectStatus.FINISHED);

        ProjectResponseDto actual = projectManagementService.closeProject(expected);

        assertEquals(expected.getProjectStatus(), actual.getStatus());
    }

    @Test
    void closeProject_ShouldThrowException_IfRequestDtoStatusNotFinished() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.IN_PROGRESS), ProjectStatus.IN_PROGRESS);

        assertThrows(ProjectClosingException.class, () -> {
            projectManagementService.closeProject(expected);
        });
    }

    @Test
    void closeProject_ShouldThrowException_IfProjectForClosingNotExists() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.IN_PROGRESS), ProjectStatus.FINISHED);
        expected.setId(expected.getId() + UNREACHABLE_ID);

        assertThrows(ProjectNotFoundException.class, () -> {
            projectManagementService.closeProject(expected);
        });
    }

    @Test
    void closeProject_ShouldThrowException_IfProjectAlreadyFinished() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.FINISHED), ProjectStatus.IN_PROGRESS);

        assertThrows(ProjectClosingException.class, () -> {
            projectManagementService.closeProject(expected);
        });
    }

    @Test
    void closeProject_ShouldThrowException_IfProjectHadNotFinishedReleases() {
        Task expectedTask = returnTaskForTestNotClosedRelease();

        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(expectedTask.getProject().getId(), ProjectStatus.FINISHED);

        assertThrows(ProjectClosingException.class, () -> {
            projectManagementService.closeProject(expected);
        });
    }

    private Task returnTaskForTestNotClosedRelease() {
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

        Task taskForSave1 = new Task(null, "TestName1", "description", TaskStatus.IN_PROGRESS, project, release, user, user);

        return taskRepository.save(taskForSave1);
    }

    @AfterEach
    public void cleanUp() {
        testUtil.clearBase();
    }

    private long returnExistingProjectId(ProjectStatus status) {
        User forTest = new User(
                UUID.randomUUID().toString(),
                "testPassword",
                "testName",
                "testAccount",
                Set.of(Role.USER)
        );
        User customer = userRepository.save(forTest);

        Project projectForSave = new Project(
                "testProject",
                status,
                customer,
                new BigDecimal("3000.00")
        );

        return projectRepository.save(projectForSave).getId();
    }
}
