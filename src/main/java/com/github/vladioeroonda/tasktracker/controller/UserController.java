package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение списка всех Пользователей")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретного Пользователя по его id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        UserResponseDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Добавление нового Пользователя")
    @PostMapping
    public ResponseEntity<UserResponseDto> addNewUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto user = userService.addUser(requestDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Пользователя")
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto user = userService.updateUser(requestDto);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретного Пользователя по его id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(
                String.format("Пользователь с id #%d был успешно удалён", id),
                HttpStatus.OK
        );
    }
}
