package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
@RequestMapping("/api/tracker/users")
public class UserController {

    private final ModelMapper modelMapper;

    public UserController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Получение списка всех Пользователей")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        UserResponseDto user1 = new UserResponseDto(
                1L,
                "user1",
                "User1 user1"
        );
        UserResponseDto user2 = new UserResponseDto(
                2L,
                "user2",
                "User2 User2"
        );

        List<UserResponseDto> users = List.of(user1, user2);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретного Пользователя по его id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        UserResponseDto user = new UserResponseDto(
                1L,
                "user1",
                "User1 user1"
        );

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Добавление нового Пользователя")
    @PostMapping
    public ResponseEntity<UserResponseDto> addNewUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto user = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Пользователя")
    @PutMapping
    public ResponseEntity<UserResponseDto> editUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto user = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретного Пользователя по его id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {

        return new ResponseEntity<>(
                String.format("Пользователь с id #%s был успешно удалён", id),
                HttpStatus.OK
        );
    }

    private UserResponseDto convertFromRequestToResponseDto(UserRequestDto requestDto) {
        return modelMapper.map(requestDto, UserResponseDto.class);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleNotFoundException(UserNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
