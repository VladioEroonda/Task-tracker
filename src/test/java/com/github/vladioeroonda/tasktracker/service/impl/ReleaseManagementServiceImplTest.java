package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.request.ReleaseClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseClosingException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
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
import com.github.vladioeroonda.tasktracker.service.ReleaseManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles(profiles = "test")
@SpringBootTest
class ReleaseManagementServiceImplTest {
    private static final int UNREACHABLE_ID = 100_000;
    @Autowired
    private ReleaseManagementService releaseManagementService;
    @Autowired
    private ReleaseRepository releaseRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TestUtil testUtil;

    @Test
    void countUnfinishedTasksByReleaseId() {
        List<Task> expectedTasks = addTwoNotFinishedTasks();
        long releaseId = expectedTasks.get(0).getRelease().getId();

        int actual = releaseManagementService.countUnfinishedTasksByReleaseId(releaseId);
        expectedTasks.stream().mapToLong(Task::getId).forEach(testUtil::deleteTaskById);

        assertEquals(actual, expectedTasks.size());
    }

    @Test
    void countUnfinishedTasksByReleaseId_ShouldThrowException_WhenReleaseNotFoundById() {
        long expectedId = addOnlyRelease();

        assertThrows(ReleaseNotFoundException.class, () -> {
            releaseManagementService.countUnfinishedTasksByReleaseId(expectedId + UNREACHABLE_ID);
        });

        deleteReleaseById(expectedId);
    }

    @Test
    void closeRelease_ShouldCloseRelease_AndChangeAllUnclosedTasksStatusToCancelled() {
        List<Task> expectedTasks = addTwoNotFinishedTasks();
        long releaseId = expectedTasks.get(0).getRelease().getId();

        ReleaseClosingRequestDto expected =
                new ReleaseClosingRequestDto(releaseId, LocalDateTime.now().plusHours(3));

        ReleaseResponseDto actual = releaseManagementService.closeRelease(expected);

        assertNotNull(actual.getFinishTime());
        assertEquals(getTaskStatusById(expectedTasks.get(0).getId()), TaskStatus.CANCELLED);
        assertEquals(getTaskStatusById(expectedTasks.get(1).getId()), TaskStatus.CANCELLED);

        expectedTasks.stream().mapToLong(Task::getId).forEach(testUtil::deleteTaskById);
    }

    @Test
    void closeRelease_ShouldThrowException_WhenReleaseNotFoundById() {
        long expectedId = addOnlyRelease();
        ReleaseClosingRequestDto requestDto =
                new ReleaseClosingRequestDto(expectedId + UNREACHABLE_ID, LocalDateTime.now().plusHours(3));

        assertThrows(ReleaseNotFoundException.class, () -> {
            releaseManagementService.closeRelease(requestDto);
        });

        deleteReleaseById(expectedId);
    }

    @Test
    void closeRelease_ShouldThrowException_WhenReleaseClosingDate_BeforeStartTime() {
        long expectedId = addOnlyRelease();
        ReleaseClosingRequestDto requestDto =
                new ReleaseClosingRequestDto(expectedId, LocalDateTime.now().minusDays(1));

        assertThrows(ReleaseClosingException.class, () -> {
            releaseManagementService.closeRelease(requestDto);
        });

        deleteReleaseById(expectedId);
    }

    private long addOnlyRelease() {
        Release release = new Release("testVersion", LocalDateTime.now(), null);
        return releaseRepository.save(release).getId();
    }

    private void deleteReleaseById(long releaseId) {
        Optional<Release> releaseForDelete = releaseRepository.findById(releaseId);
        releaseForDelete.ifPresent(release -> releaseRepository.delete(release));
    }

    private List<Task> addTwoNotFinishedTasks() {
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
        Task taskForSave1 = new Task("TestName2", description, TaskStatus.IN_PROGRESS, project, release, user);
        Task taskForSave2 = new Task("TestName3", description, TaskStatus.BACKLOG, project, release, user);
        List<Task> tasks = new ArrayList<>();
        tasks.add(taskRepository.save(taskForSave1));
        tasks.add(taskRepository.save(taskForSave2));
        return tasks;
    }

    private TaskStatus getTaskStatusById(long taskId) {
        Optional<Task> taskFromBD = taskRepository.findById(taskId);
        if (taskFromBD.isPresent()) {
            return taskFromBD.get().getStatus();
        } else throw new TaskNotFoundException("Такой задачи нет");
    }
}
