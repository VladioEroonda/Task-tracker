package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.Util.TestUtil;
import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.exception.UserBadDataException;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Role;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles(profiles = "test")
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest
class UserServiceImplTest {
    private static final int UNREACHABLE_ID = 100_000;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtil testUtil;

    @Test
    void getAllUsers() {
        long expectedUserId = addTestUser();

        List<UserResponseDto> actualUsers = userService.getAllUsers();

        assertTrue(actualUsers.size() > 0);
    }

    @Test
    void getUserByIdAndReturnResponseDto() {
        long expectedUserId = addTestUser();

        UserResponseDto actual = userService.getUserByIdAndReturnResponseDto(expectedUserId);

        assertEquals(expectedUserId, actual.getId());
    }

    @Test
    void getUserByIdAndReturnResponseDto_ShouldThrowException_IfUserNotExists() {
        long expectedUserId = addTestUser();

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByIdAndReturnResponseDto(expectedUserId + UNREACHABLE_ID);
        });
    }

    @Test
    void getUserByIdAndReturnEntity() {
        long expectedUserId = addTestUser();

        User actual = userService.getUserByIdAndReturnEntity(expectedUserId);

        assertEquals(expectedUserId, actual.getId());
    }

    @Test
    void getUserByIdAndReturnEntity_ShouldThrowException_IfUserNotExists() {
        long expectedUserId = addTestUser();

        assertThrows(UserNotFoundException.class, () -> {
            userService.checkUserExistsById(expectedUserId + UNREACHABLE_ID);
        });
    }

    @Test
    void checkUserExistsById_ShouldNotThrowException_IfUserExists() {
        long expectedUserId = addTestUser();

        userService.checkUserExistsById(expectedUserId);
    }

    @Test
    void checkUserExistsById_ShouldThrowException_IfUserNotExists() {
        long expectedUserId = addTestUser();

        assertThrows(UserNotFoundException.class, () -> {
            userService.checkUserExistsById(expectedUserId + UNREACHABLE_ID);
        });
    }

    @Test
    void addUser() {
        UserRequestDto expected = new UserRequestDto(
                null,
                UUID.randomUUID().toString(),
                "testPassword",
                "testName",
                Set.of(Role.USER),
                "testAccount"
        );

        UserResponseDto actual = userService.addUser(expected);

        assertEquals(expected.getName(), actual.getName());
        assertNotNull(actual.getId());
    }

    @Test
    void addUser_ShouldThrowException_IfLoginAlreadyExists() {
        long expectedId = addTestUser();
        String expectedLogin = returnLoginById(expectedId);
        UserRequestDto expected = new UserRequestDto(
                null,
                expectedLogin,
                "testPassword",
                "testName",
                Set.of(Role.USER),
                "testAccount"
        );

        assertThrows(UserBadDataException.class, () -> {
            userService.addUser(expected);
        });
    }

    @Test
    void updateUser() {
        long addedId = addTestUser();
        String randomName = UUID.randomUUID().toString();
        UserRequestDto expected = new UserRequestDto(
                addedId,
                UUID.randomUUID().toString(),
                "testPassword",
                randomName,
                Set.of(Role.USER),
                "testAccount"
        );
        UserResponseDto actual = userService.updateUser(expected);

        assertEquals(randomName, actual.getName());
    }

    @Test
    void updateUser_ShouldThrowException_IfUpdatedUserNotExists() {
        long addedId = addTestUser();
        System.out.println(addedId);
        String randomName = UUID.randomUUID().toString();
        UserRequestDto expected = new UserRequestDto(
                addedId + UNREACHABLE_ID,
                UUID.randomUUID().toString(),
                "testPassword",
                randomName,
                Set.of(Role.USER),
                "testAccount"
        );

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(expected);
        });
    }

    @Test
    void updateUser_ShouldThrowException_IfUpdatedLoginIsBusy() {
        long addedUserId = addTestUser();
        String loginFromBD = returnLoginById(addedUserId);
        long anotherAddedUserId = addTestUser();
        String randomName = UUID.randomUUID().toString();
        UserRequestDto expected = new UserRequestDto(
                anotherAddedUserId,
                loginFromBD,
                "testPassword",
                randomName,
                Set.of(Role.USER),
                "testAccount"
        );

        assertThrows(UserBadDataException.class, () -> {
            userService.updateUser(expected);
        });
    }

    @Test
    void deleteUser_ShouldNotThrowException_IfUserDeletedSuccessfully() {
        long expectedId = addTestUser();

        userService.deleteUser(expectedId);
    }

    @Test
    void deleteUser_ShouldThrowException_IfUserNotExists() {
        long expectedId = addTestUser();

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(expectedId + UNREACHABLE_ID);
        });
    }

    @AfterEach
    public void cleanUp() {
        testUtil.clearBase();
    }

    private long addTestUser() {
        User userForTest = new User(
                UUID.randomUUID().toString(),
                "testPassword",
                "testName",
                "testAccount",
                Set.of(Role.USER)
        );
        return userRepository.save(userForTest).getId();
    }

    private String returnLoginById(long id) {
        Optional<User> userFromBd = userRepository.findById(id);
        if (userFromBd.isPresent()) {
            return userFromBd.get().getLogin();
        } else throw new UserNotFoundException("Пользователь не найден");
    }
}
