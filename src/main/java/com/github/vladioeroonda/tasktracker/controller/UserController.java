package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.service.UserService;
import com.github.vladioeroonda.tasktracker.service.impl.ProjectManagementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Пользователь", description = "Отвечает за CRUD операции, связанные с Пользователем")
@RestController
@RequestMapping("/api/tracker/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение списка всех Пользователей")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        logger.info("GET /api/tracker/user");

        List<UserResponseDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретного Пользователя по его id")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        logger.info("GET /api/tracker/user/{id}");
        UserResponseDto user = userService.getUserByIdAndReturnResponseDto(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Добавление нового Пользователя")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> addNewUser(@RequestBody UserRequestDto requestDto) {
        logger.info("POST /api/tracker/user");
        UserResponseDto user = userService.addUser(requestDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Пользователя")
    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto requestDto) {
        logger.info("PUT /api/tracker/user");
        UserResponseDto user = userService.updateUser(requestDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретного Пользователя по его id")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        logger.info("DELETE /api/tracker/user/{id}");
        userService.deleteUser(id);
        return new ResponseEntity<>(
                String.format("Пользователь с id #%d был успешно удалён", id),
                HttpStatus.OK
        );
    }
}
