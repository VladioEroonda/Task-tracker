package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/tracker")
public class UserController {

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/users")
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

    @GetMapping(value = "/users/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        UserResponseDto user = new UserResponseDto(
                1L,
                "user1",
                "User1 user1"
        );

        if (user == null) {
            throw new UserNotFoundException("Пользователя с id #" + id + " не существует");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserResponseDto> addNewUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto user = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<UserResponseDto> editUser(@RequestBody UserRequestDto requestDto) {
        UserResponseDto user = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        String info = "Пользователь с id #" + id + " был успешно удалён";

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    private UserResponseDto convertFromRequestToResponseDto(UserRequestDto requestDto) {
        return modelMapper.map(requestDto, UserResponseDto.class);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handleNotFoundException(UserNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
