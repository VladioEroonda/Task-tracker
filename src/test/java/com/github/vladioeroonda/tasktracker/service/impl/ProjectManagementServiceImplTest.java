package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectClosingException;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Project;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import com.github.vladioeroonda.tasktracker.model.Release;
import com.github.vladioeroonda.tasktracker.model.Role;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.ProjectRepository;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectManagementService;
import com.github.vladioeroonda.tasktracker.service.ReleaseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
class ProjectManagementServiceImplTest {
    private static int UNREACHABLE_ID = 100_000;
    @Autowired
    private ProjectManagementService projectManagementService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @MockBean
    private ReleaseService releaseService;

    @Test
    void closeProject() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.IN_PROGRESS), ProjectStatus.FINISHED);

        ProjectResponseDto actual = projectManagementService.closeProject(expected);

        assertEquals(expected.getProjectStatus(), actual.getStatus());

        deleteTestProjectAndUser(expected.getId());
    }

    @Test
    void closeProject_ShouldThrowException_IfRequestDtoStatusNotFinished() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.IN_PROGRESS), ProjectStatus.IN_PROGRESS);

        assertThrows(ProjectClosingException.class, () -> {
            projectManagementService.closeProject(expected);
        });

        deleteTestProjectAndUser(expected.getId());
    }

    @Test
    void closeProject_ShouldThrowException_IfProjectForClosingNotExists() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.IN_PROGRESS), ProjectStatus.FINISHED);
        expected.setId(expected.getId() + UNREACHABLE_ID);

        assertThrows(ProjectNotFoundException.class, () -> {
            projectManagementService.closeProject(expected);
        });

        deleteTestProjectAndUser(expected.getId());
    }

    @Test
    void closeProject_ShouldThrowException_IfProjectAlreadyFinished() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.FINISHED), ProjectStatus.IN_PROGRESS);

        assertThrows(ProjectClosingException.class, () -> {
            projectManagementService.closeProject(expected);
        });

        deleteTestProjectAndUser(expected.getId());
    }

    @Test
    void closeProject_ShouldThrowException_IfProjectHadNotFinishedReleases() {
        ProjectClosingRequestDto expected =
                new ProjectClosingRequestDto(returnExistingProjectId(ProjectStatus.IN_PROGRESS), ProjectStatus.FINISHED);

        Mockito.when(releaseService.getAllNotClosedReleasesByProjectId(expected.getId())).thenReturn(List.of(new Release(), new Release()));

        assertThrows(ProjectClosingException.class, () -> {
            projectManagementService.closeProject(expected);
        });

        deleteTestProjectAndUser(expected.getId());
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

    private void deleteTestProjectAndUser(long projectId) {
        Optional<Project> projectFromBD = projectRepository.findById(projectId);
        if (projectFromBD.isPresent()) {
            projectRepository.delete(projectFromBD.get());
            Optional<User> userForDelete = userRepository.findById(projectFromBD.get().getCustomer().getId());
            userForDelete.ifPresent(user -> userRepository.delete(user));
        }
    }
}
