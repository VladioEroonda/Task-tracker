package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseBadDataException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
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
import com.github.vladioeroonda.tasktracker.service.ReleaseService;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(profiles = "test")
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
class ReleaseServiceImplTest {
    private static final int UNREACHABLE_ID = 100_000;
    @Autowired
    private ReleaseService releaseService;
    @Autowired
    private ReleaseRepository releaseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TestUtil testUtil;

    @Test
    void getAllReleases() {
        long expectedId = returnAddedReleaseId();
        long expectedId2 = returnAddedReleaseId();

        List<ReleaseResponseDto> allReleases = releaseService.getAllReleases();

        assertTrue(allReleases.size() >= 2);
    }

    @Test
    void getReleaseByIdAndReturnResponseDto() {
        long expectedId = returnAddedReleaseId();

        ReleaseResponseDto actual = releaseService.getReleaseByIdAndReturnResponseDto(expectedId);

        assertNotNull(actual);
    }

    @Test
    void getReleaseByIdAndReturnResponseDto_ShouldThrowException_IfReleaseNotExists() {
        long expectedId = returnAddedReleaseId();

        assertThrows(ReleaseNotFoundException.class, () -> {
            releaseService.getReleaseByIdAndReturnResponseDto(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void getReleaseByIdAndReturnEntity() {
        long expectedId = returnAddedReleaseId();

        Release actual = releaseService.getReleaseByIdAndReturnEntity(expectedId);

        assertNotNull(actual);
    }

    @Test
    void getReleaseByIdAndReturnEntity_ShouldThrowException_IfReleaseNotExists() {
        long expectedId = returnAddedReleaseId();

        assertThrows(ReleaseNotFoundException.class, () -> {
            releaseService.getReleaseByIdAndReturnEntity(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void checkReleaseExistsById_ShouldNotThrowException_IfReleaseExists() {
        long expectedId = returnAddedReleaseId();

        releaseService.checkReleaseExistsById(expectedId);
    }

    @Test
    void checkReleaseExistsById_ShouldThrowException_IfReleaseNotExists() {
        long expectedId = returnAddedReleaseId();

        assertThrows(ReleaseNotFoundException.class, () -> {
            releaseService.checkReleaseExistsById(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void addRelease() {
        ReleaseRequestDto expected = new ReleaseRequestDto(
                null,
                UUID.randomUUID().toString(),
                LocalDateTime.now()
        );

        ReleaseResponseDto actual = releaseService.addRelease(expected);

        assertNotNull(actual);
        assertEquals(expected.getVersion(), actual.getVersion());
    }

    @Test
    void updateRelease_ShouldBeSuccessfully() {
        long expectedId = returnAddedReleaseId();
        String expectedVersion = UUID.randomUUID().toString();
        LocalDateTime expectedDate = LocalDateTime.now();
        ReleaseRequestDto expected = new ReleaseRequestDto(
                expectedId,
                expectedVersion,
                expectedDate
        );

        ReleaseResponseDto actual = releaseService.updateRelease(expected);

        assertNotNull(actual);
        assertEquals(expectedVersion, actual.getVersion());
        assertEquals(expectedDate, actual.getStartTime());
    }

    @Test
    void updateRelease_ShouldThrowException_IfReleaseNotExistsByThisId() {
        long expectedId = returnAddedReleaseId();
        String expectedVersion = UUID.randomUUID().toString();
        LocalDateTime expectedDate = LocalDateTime.now();
        ReleaseRequestDto expected = new ReleaseRequestDto(
                expectedId + UNREACHABLE_ID,
                expectedVersion,
                expectedDate
        );

        assertThrows(ReleaseNotFoundException.class, () -> {
            releaseService.updateRelease(expected);
        });
    }

    @Test
    void updateRelease_ShouldThrowException_IfReleaseHaveClosingTime() {
        long expectedId = returnAddedReleaseId();

        ReleaseRequestDto expected = new ReleaseRequestDto(
                expectedId,
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(3)
        );

        assertThrows(ReleaseBadDataException.class, () -> {
            releaseService.updateRelease(expected);
        });
    }

    @Test
    void deleteRelease_ShouldNotThrowException_IfReleaseExists() {
        long expectedId = returnAddedReleaseId();

        releaseService.deleteRelease(expectedId);
    }

    @Test
    void deleteRelease_ShouldThrowException_IfReleaseNotExists() {
        long expectedId = returnAddedReleaseId();

        assertThrows(ReleaseNotFoundException.class, () -> {
            releaseService.deleteRelease(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void getAllNotClosedReleasesByProjectId_ShouldBeSuccessfully_Return2() {
        List<Task> expectedTasks = addTestTasksFromOneProjectAndTwoReleases();
        long expectedProjectId = expectedTasks.get(0).getProject().getId();

        List<Release> actual = releaseService.getAllNotClosedReleasesByProjectId(expectedProjectId);

        assertEquals(2, actual.size());
    }

    @Test
    void getAllNotClosedReleasesByProjectId() {
        long expectedProjectId = returnAddedProjectId();

        assertThrows(ProjectNotFoundException.class, () -> {
            releaseService.getAllNotClosedReleasesByProjectId(expectedProjectId + UNREACHABLE_ID);
        });
    }

    @AfterEach
    public void cleanUp() {
        testUtil.clearBase();
    }

    private long returnAddedReleaseId() {
        Release releaseForTest = new Release(UUID.randomUUID().toString(), LocalDateTime.now(), null);
        return releaseRepository.save(releaseForTest).getId();
    }

    private long returnAddedProjectId() {
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
        return projectRepository.save(project).getId();
    }

    private List<Task> addTestTasksFromOneProjectAndTwoReleases() {
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

        Release releaseNotClosed = new Release(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                null
        );
        releaseRepository.save(releaseNotClosed);

        Release releaseAlreadyClosed = new Release(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                null
        );
        releaseRepository.save(releaseAlreadyClosed);

        Task taskForSave1 = new Task(null, "TestName1", "description", TaskStatus.IN_PROGRESS, project, releaseNotClosed, user, user);
        Task taskForSave2 = new Task("TestName2", "description", TaskStatus.IN_PROGRESS, project, releaseNotClosed, user);
        Task taskForSave3 = new Task("TestName3", "description", TaskStatus.IN_PROGRESS, project, releaseAlreadyClosed, user);
        List<Task> tasks = new ArrayList<>();
        tasks.add(taskRepository.save(taskForSave1));
        tasks.add(taskRepository.save(taskForSave2));
        tasks.add(taskRepository.save(taskForSave3));
        return tasks;
    }
}
