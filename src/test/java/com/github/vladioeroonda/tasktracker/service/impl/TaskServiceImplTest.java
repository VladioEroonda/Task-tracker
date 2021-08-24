package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.TaskRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.TaskBadDataException;
import com.github.vladioeroonda.tasktracker.exception.TaskNotFoundException;
import com.github.vladioeroonda.tasktracker.exception.WrongFileTypeException;
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
import com.github.vladioeroonda.tasktracker.service.TaskService;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(profiles = "test")
@SpringBootTest
class TaskServiceImplTest {
    private static int UNREACHABLE_ID = 100_000;
    @Autowired
    private TaskService taskService;
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
    void getAllTasks() {
        Task expectedTask1 = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId1 = expectedTask1.getId();
        Task expectedTask2 = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId2 = expectedTask2.getId();

        List<TaskResponseDto> actual = taskService.getAllTasks();
        testUtil.deleteTaskById(expectedTaskId1);
        testUtil.deleteTaskById(expectedTaskId2);

        assertTrue(actual.size() >= 2);
    }

    @Test
    void getTaskByIdAndReturnResponseDto() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        TaskResponseDto actual = taskService.getTaskByIdAndReturnResponseDto(expectedTaskId);
        testUtil.deleteTaskById(expectedTaskId);

        assertNotNull(actual);
        assertEquals(expectedTaskId, actual.getId());
    }

    @Test
    void getTaskByIdAndReturnResponseDto_ShouldThrowException_WhenTaskNotExists() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByIdAndReturnResponseDto(expectedTaskId + UNREACHABLE_ID);
        });

        testUtil.deleteTaskById(expectedTaskId);
    }

    @Test
    void getTaskByIdAndReturnEntity() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        Task actual = taskService.getTaskByIdAndReturnEntity(expectedTaskId);
        testUtil.deleteTaskById(expectedTaskId);

        assertNotNull(actual);
        assertEquals(expectedTaskId, actual.getId());
    }

    @Test
    void getTaskByIdAndReturnEntity_ShouldThrowException_WhenTaskNotExists() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByIdAndReturnEntity(expectedTaskId + UNREACHABLE_ID);
        });

        testUtil.deleteTaskById(expectedTaskId);
    }

    @Test
    void checkTaskExistsById_ShouldNotThrowException() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        taskService.checkTaskExistsById(expectedTaskId);

        testUtil.deleteTaskById(expectedTaskId);
    }


    @Test
    void checkTaskExistsById_ShouldThrowException_WhenTaskNotExists() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.checkTaskExistsById(expectedTaskId + UNREACHABLE_ID);
        });

        testUtil.deleteTaskById(expectedTaskId);
    }

    @Test
    void addTask_ShouldBeSuccessful() {
        Task existingTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        UserRequestDto expectedUser = new UserRequestDto(existingTask.getAuthor().getId());
        ProjectRequestDto expectedProject = returnFormedProjectDto(existingTask.getProject().getId(), expectedUser);
        ReleaseRequestDto expectedRelease = new ReleaseRequestDto(existingTask.getRelease().getId());
        TaskRequestDto expectedTask =
                returnFormedTaskDto("testTaskName", "testDescription", expectedProject, expectedRelease, expectedUser, null);

        TaskResponseDto actual = taskService.addTask(expectedTask);
        testUtil.deleteTaskById(existingTask.getId());

        assertNotNull(actual);
        assertEquals(expectedTask.getName(), actual.getName());
        assertEquals(expectedTask.getDescription(), actual.getDescription());
        assertEquals(TaskStatus.BACKLOG, actual.getStatus());
    }

    @Test
    void addTask_ShouldBeSuccessful_ShouldThrowException_IfTaskNameLength_LessThenValueFromProperties() {
        Task existingTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);

        UserRequestDto expectedUser = new UserRequestDto(existingTask.getAuthor().getId());
        ProjectRequestDto expectedProject = returnFormedProjectDto(existingTask.getProject().getId(), expectedUser);
        ReleaseRequestDto expectedRelease = new ReleaseRequestDto(existingTask.getRelease().getId());
        TaskRequestDto expectedTask =
                returnFormedTaskDto("test", "testDescription", expectedProject, expectedRelease, expectedUser, null);

        assertThrows(TaskBadDataException.class, () -> {
            taskService.addTask(expectedTask);
        });

        testUtil.deleteTaskById(existingTask.getId());
    }

    @Test
    void addTask_ShouldBeSuccessful_ShouldThrowException_IfTaskDescriptionLength_LessThenValueFromProperties() {
        Task existingTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);

        UserRequestDto expectedUser = new UserRequestDto(existingTask.getAuthor().getId());
        ProjectRequestDto expectedProject = returnFormedProjectDto(existingTask.getProject().getId(), expectedUser);
        ReleaseRequestDto expectedRelease = new ReleaseRequestDto(existingTask.getRelease().getId());
        TaskRequestDto expectedTask =
                returnFormedTaskDto("testTaskName", "test", expectedProject, expectedRelease, expectedUser, null);

        assertThrows(TaskBadDataException.class, () -> {
            taskService.addTask(expectedTask);
        });

        testUtil.deleteTaskById(existingTask.getId());
    }

    @Test
    void addTask_ShouldBeSuccessful_ShouldThrowException_IfProjectAlreadyFinished() {
        Task existingTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.FINISHED, null);

        UserRequestDto expectedUser = new UserRequestDto(existingTask.getAuthor().getId());
        ProjectRequestDto expectedProject = returnFormedProjectDto(existingTask.getProject().getId(), expectedUser);
        ReleaseRequestDto expectedRelease = new ReleaseRequestDto(existingTask.getRelease().getId());
        TaskRequestDto expectedTask =
                returnFormedTaskDto("testTaskName", "testDescription", expectedProject, expectedRelease, expectedUser, null);

        assertThrows(TaskBadDataException.class, () -> {
            taskService.addTask(expectedTask);
        });

        testUtil.deleteTaskById(existingTask.getId());
    }

    @Test
    void addTask_ShouldBeSuccessful_ShouldThrowException_IfReleaseAlreadyFinished() {
        Task existingTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, LocalDateTime.now().plusMinutes(10));

        UserRequestDto expectedUser = new UserRequestDto(existingTask.getAuthor().getId());
        ProjectRequestDto expectedProject = returnFormedProjectDto(existingTask.getProject().getId(), expectedUser);
        ReleaseRequestDto expectedRelease = new ReleaseRequestDto(existingTask.getRelease().getId());
        TaskRequestDto expectedTask =
                returnFormedTaskDto("testTaskName", "testDescription", expectedProject, expectedRelease, expectedUser, null);

        assertThrows(TaskBadDataException.class, () -> {
            taskService.addTask(expectedTask);
        });

        testUtil.deleteTaskById(existingTask.getId());
    }

    @Test
    void addTaskByCsv() {
        MultipartFile expected = csvForTest();

        if (Objects.isNull(expected)) {
            throw new WrongFileTypeException("Error while file init");
        }

        List<TaskResponseDto> actual = taskService.addTaskByCsv(expected);
        actual.stream().mapToLong(TaskResponseDto::getId).forEach(this::deleteOnlyTaskById);

        assertNotNull(actual);
        assertEquals(1, actual.size());
    }

    @Test
    void deleteTask() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        taskService.deleteTask(expectedTaskId);

        testUtil.deleteTaskById(expectedTaskId);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotExists() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(expectedTaskId + UNREACHABLE_ID);
        });

        testUtil.deleteTaskById(expectedTaskId);
    }

    @Test
    void countUnfinishedTasksByReleaseId_ShouldBe1() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);
        long expectedTaskId = expectedTask.getId();

        int actual = taskService.countUnfinishedTasksByReleaseId(expectedTask.getRelease().getId());
        testUtil.deleteTaskById(expectedTask.getId());

        assertEquals(1, actual);
    }

    @Test
    void countUnfinishedTasksByReleaseId_ShouldBeZero() {
        Task expectedTask = returnNewTask(TaskStatus.DONE, ProjectStatus.IN_PROGRESS, null);

        int actual = taskService.countUnfinishedTasksByReleaseId(expectedTask.getRelease().getId());
        testUtil.deleteTaskById(expectedTask.getId());

        assertEquals(0, actual);
    }

    @Test
    void setAllTasksCancelled() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);

        taskService.setAllTasksCancelled(expectedTask.getRelease().getId());

        TaskStatus actual = getTaskStatusById(expectedTask.getId());
        testUtil.deleteTaskById(expectedTask.getId());

        assertEquals(TaskStatus.CANCELLED, actual);
    }

    @Test
    void setAllTasksCancelled_ShouldThrowException_WhenReleaseNotExists() {
        Task expectedTask = returnNewTask(TaskStatus.IN_PROGRESS, ProjectStatus.IN_PROGRESS, null);

        assertThrows(ReleaseNotFoundException.class, () -> {
            taskService.setAllTasksCancelled(expectedTask.getRelease().getId() + UNREACHABLE_ID);
        });

        testUtil.deleteTaskById(expectedTask.getId());
    }

    private TaskRequestDto returnFormedTaskDto(String name, String description, ProjectRequestDto project, ReleaseRequestDto release, UserRequestDto author, UserRequestDto executor) {
        return new TaskRequestDto(
                null,
                name,
                description,
                TaskStatus.BACKLOG,
                project,
                release,
                author,
                executor
        );
    }

    private ProjectRequestDto returnFormedProjectDto(long projectId, UserRequestDto user) {
        return new ProjectRequestDto(
                projectId,
                UUID.randomUUID().toString(),
                ProjectStatus.IN_PROGRESS,
                user,
                new BigDecimal("3000"));
    }

    private Task returnNewTask(TaskStatus taskStatus, ProjectStatus projectStatus, LocalDateTime projectFinishTime) {
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
                projectStatus,
                user,
                new BigDecimal("3000")
        );
        projectRepository.save(project);

        Release release = new Release(
                UUID.randomUUID().toString(),
                LocalDateTime.now(),
                projectFinishTime
        );
        releaseRepository.save(release);
        Task taskForSave = new Task("testTaskName", "testDescription", taskStatus, project, release, user);

        return taskRepository.save(taskForSave);
    }

    private void deleteOnlyTaskById(long taskId) {
        taskRepository.findById(taskId).ifPresent(
                (task -> {
                    taskRepository.delete(task);
                })
        );
    }

    private TaskStatus getTaskStatusById(long taskId) {
        Optional<Task> taskFromBD = taskRepository.findById(taskId);
        if (taskFromBD.isPresent()) {
            return taskFromBD.get().getStatus();
        } else throw new TaskNotFoundException("Такой задачи нет");
    }

    private MultipartFile csvForTest() {
        File file = new File("src/test/resources/input.csv");
        MultipartFile multipartFile = null;
        try (FileInputStream input = new FileInputStream(file);) {
            multipartFile = new MockMultipartFile("file",
                    file.getName(), "text/csv", IOUtils.toByteArray(input));
            return multipartFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return multipartFile;
    }
}
