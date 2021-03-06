package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectBadDataException;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.feign.PaymentClient;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.Role;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
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
class ProjectServiceImplTest {
    private static final int UNREACHABLE_ID = 100_000;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtil testUtil;
    @MockBean
    private PaymentClient paymentClient;

    @Test
    void addProject_ShouldBeSuccessful() {
        long expectedUserId = returnSavedUserId("anyId");
        UserRequestDto expectedUser =
                new UserRequestDto(
                        expectedUserId,
                        "TestLogin",
                        "TestPassword",
                        "TestName",
                        Set.of(Role.USER),
                        "notBlankUserAccountId"
                );
        ProjectRequestDto expected =
                new ProjectRequestDto(null, "TestProject", ProjectStatus.IN_PROGRESS, expectedUser, new BigDecimal("111.11"));
        Mockito.when(paymentClient.getPaymentCheckResult(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(true);

        ProjectResponseDto actual = projectService.addProject(expected);

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void addProject_ShouldThrowException_IfCustomerIsNull() {
        long expectedUserId = returnSavedUserId(null);
        UserRequestDto expectedUser =
                new UserRequestDto(
                        expectedUserId,
                        "TestLogin",
                        "TestPassword",
                        "TestName",
                        Set.of(Role.USER),
                        null
                );
        ProjectRequestDto expected =
                new ProjectRequestDto(null, "TestProject", ProjectStatus.IN_PROGRESS, null, new BigDecimal("111.11"));
        Mockito.when(paymentClient.getPaymentCheckResult(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(true);

        assertThrows(ProjectBadDataException.class, () -> {
            projectService.addProject(expected);
        });
    }

    @Test
    void addProject_ShouldThrowException_IfUsersBankAccountIsNull() {
        long expectedUserId = returnSavedUserId(null);
        UserRequestDto expectedUser =
                new UserRequestDto(
                        expectedUserId,
                        "TestLogin",
                        "TestPassword",
                        "TestName",
                        Set.of(Role.USER),
                        null
                );
        ProjectRequestDto expected =
                new ProjectRequestDto(null, "TestProject", ProjectStatus.IN_PROGRESS, expectedUser, new BigDecimal("111.11"));
        Mockito.when(paymentClient.getPaymentCheckResult(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(true);

        assertThrows(ProjectBadDataException.class, () -> {
            projectService.addProject(expected);
        });
    }

    @Test
    void addProject_ShouldThrowException_IfProjectWasNotPaid() {
        long expectedUserId = returnSavedUserId("anyId");
        UserRequestDto expectedUser =
                new UserRequestDto(
                        expectedUserId,
                        "TestLogin",
                        "TestPassword",
                        "TestName",
                        Set.of(Role.USER),
                        null
                );
        ProjectRequestDto expected =
                new ProjectRequestDto(null, "TestProject", ProjectStatus.IN_PROGRESS, expectedUser, new BigDecimal("111.11"));
        Mockito.when(paymentClient.getPaymentCheckResult(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(false);

        assertThrows(ProjectBadDataException.class, () -> {
            projectService.addProject(expected);
        });
    }

    @Test
    void getProjectByIdAndReturnResponseDto() {
        long expectedId = returnAddedProject().getId();

        ProjectResponseDto actual = projectService.getProjectByIdAndReturnResponseDto(expectedId);

        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
    }

    @Test
    void getProjectByIdAndReturnResponseDto_ShouldThrowException_IfProjectNotExistsById() {
        long expectedId = returnAddedProject().getId();

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.getProjectByIdAndReturnResponseDto(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void getProjectByIdAndReturnEntity() {
        long expectedId = returnAddedProject().getId();

        Project actual = projectService.getProjectByIdAndReturnEntity(expectedId);

        assertNotNull(actual);
        assertEquals(expectedId, actual.getId());
    }

    @Test
    void getProjectByIdAndReturnEntity_ShouldThrowException_IfProjectNotExistsById() {
        long expectedId = returnAddedProject().getId();

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.getProjectByIdAndReturnEntity(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void checkProjectExistsById_ShouldBeSuccessful_IfProjectExists() {
        long expectedId = returnAddedProject().getId();

        projectService.checkProjectExistsById(expectedId);
    }

    @Test
    void checkProjectExistsById_ShouldThrowException_IfProjectNotExistsById() {
        long expectedId = returnAddedProject().getId();

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.checkProjectExistsById(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void updateProject() {
        Project createdProject = returnAddedProject();
        UserRequestDto expectedUser =
                new UserRequestDto(createdProject.getCustomer().getId());
        ProjectRequestDto expectedProject = new ProjectRequestDto(
                createdProject.getId(),
                "testName",
                ProjectStatus.IN_PROGRESS,
                expectedUser,
                new BigDecimal("2500")
        );

        ProjectResponseDto actual = projectService.updateProject(expectedProject);

        assertNotNull(actual);
        assertEquals(expectedProject.getName(), actual.getName());
        assertEquals(expectedProject.getStatus(), actual.getStatus());
    }

    @Test
    void updateProject_ShouldThrowException_IfProjectNotExistsById() {
        Project createdProject = returnAddedProject();
        UserRequestDto expectedUser =
                new UserRequestDto(createdProject.getCustomer().getId());
        ProjectRequestDto expectedProject = new ProjectRequestDto(
                createdProject.getId() + UNREACHABLE_ID,
                "testName",
                ProjectStatus.IN_PROGRESS,
                expectedUser,
                new BigDecimal("2500")
        );

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.updateProject(expectedProject);
        });
    }

    @Test
    void deleteProject() {
        Project expectedProject = returnAddedProject();
        long expectedUserId = expectedProject.getCustomer().getId();

        projectService.deleteProject(expectedProject.getId());
    }

    @Test
    void deleteProject_ShouldThrowException_IfProjectNotExistsById() {
        long expectedId = returnAddedProject().getId();

        assertThrows(ProjectNotFoundException.class, () -> {
            projectService.deleteProject(expectedId + UNREACHABLE_ID);
        });
    }

    @Test
    void getAllProjects() {
        long expectedIdProject1 = returnAddedProject().getId();
        long expectedIdProject2 = returnAddedProject().getId();

        List<ProjectResponseDto> actual = projectService.getAllProjects();

        assertTrue(actual.size() >= 2);
    }

    @AfterEach
    public void cleanUp() {
        testUtil.clearBase();
    }

    private Project returnAddedProject() {
        User userForTest = new User(
                UUID.randomUUID().toString(),
                "testPassword",
                "testName",
                "testAccount",
                Set.of(Role.USER)
        );
        userRepository.save(userForTest);

        Project project = new Project(
                "TestName",
                ProjectStatus.IN_PROGRESS,
                userForTest,
                new BigDecimal("3000")
        );
        return projectRepository.save(project);
    }

    private long returnSavedUserId(String bankAccountId) {
        User userForTest = new User(
                UUID.randomUUID().toString(),
                "testPassword",
                "testName",
                bankAccountId,
                Set.of(Role.USER)
        );
        return userRepository.save(userForTest).getId();
    }
}
