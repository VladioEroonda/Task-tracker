package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Role;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException(String.format("Пользователь с id #%s не существует", id))
                );

        return convertFromEntityToResponse(user);
    }

    @Transactional
    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        User userForSave = convertFromRequestToEntity(userRequestDto);
        userForSave.setId(null);
        userForSave.setRoles(Set.of(Role.USER));

        User savedUser = userRepository.save(userForSave);
        return convertFromEntityToResponse(savedUser);
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        User userFromDB = userRepository
                .findById(userRequestDto.getId())
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Пользователь с id #%s не существует", userRequestDto.getId())
                        )
                );
        User userForSave = convertFromRequestToEntity(userRequestDto);
        userForSave.setTasksAsAuthor(userFromDB.getTasksAsAuthor());
        userForSave.setTasksAsExecutor(userFromDB.getTasksAsExecutor());

        User updatedUser = userRepository.save(userForSave);
        return convertFromEntityToResponse(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("Пользователь с id #%s не существует. Удаление невозможно", id)
                        )
                );

        userRepository.delete(user);
    }

    private User convertFromRequestToEntity(UserRequestDto requestDto) {
        return modelMapper.map(requestDto, User.class);
    }

    private UserResponseDto convertFromEntityToResponse(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
