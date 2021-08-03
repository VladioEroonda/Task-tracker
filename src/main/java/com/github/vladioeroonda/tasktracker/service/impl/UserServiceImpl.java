package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.exception.UserBadDataException;
import com.github.vladioeroonda.tasktracker.exception.UserNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Role;
import com.github.vladioeroonda.tasktracker.model.User;
import com.github.vladioeroonda.tasktracker.repository.UserRepository;
import com.github.vladioeroonda.tasktracker.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<UserResponseDto> getAllUsers() {
        logger.info("Получение списка всех Пользователей");

        List<User> users = userRepository.findAll();
        return users.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public UserResponseDto getUserByIdAndReturnResponseDto(Long id) {
        logger.info(String.format("Получение Пользователя с id #%d", id));

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> {
                    UserNotFoundException exception =
                            new UserNotFoundException(String.format("Пользователь с id #%d не существует", id));
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });

        return convertFromEntityToResponse(user);
    }

    @Transactional
    @Override
    public User getUserByIdAndReturnEntity(Long id) {
        logger.info(String.format("Получение Пользователя с id #%d", id));

        return userRepository
                .findById(id)
                .orElseThrow(() -> {
                    UserNotFoundException exception =
                            new UserNotFoundException(String.format("Пользователь с id #%d не существует", id));
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });
    }

    @Transactional
    @Override
    public void checkUserExistsById(Long id) {
        logger.info(String.format("Проверка существования Пользователя с id #%d", id));

        if (userRepository.findById(id).isEmpty()) {
            UserNotFoundException exception =
                    new UserNotFoundException(String.format("Пользователь с id #%d не существует.", id));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @Transactional
    @Override
    public UserResponseDto addUser(UserRequestDto userRequestDto) {
        logger.info("Добавление Пользователя");

        if (userRepository.getUserByLogin(userRequestDto.getLogin()).isPresent()) {
            UserBadDataException exception =
                    new UserBadDataException("Пользователь с таким логином уже существует");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        User userForSave = convertFromRequestToEntity(userRequestDto);
        userForSave.setId(null);
        userForSave.setRoles(Set.of(Role.USER));

        User savedUser = userRepository.save(userForSave);
        return convertFromEntityToResponse(savedUser);
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(UserRequestDto userRequestDto) {
        logger.info(String.format("Обновление данных Пользователя с id #%d", userRequestDto.getId()));

        User userFromDB = userRepository
                .findById(userRequestDto.getId())
                .orElseThrow(() -> {
                    UserNotFoundException exception =
                            new UserNotFoundException(
                                    String.format(
                                            "Пользователь с id #%d не существует",
                                            userRequestDto.getId()
                                    )
                            );
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });

        if (userFromDB.getLogin().equals(userRequestDto.getLogin())) {
            UserBadDataException exception =
                    new UserBadDataException("Пользователь с таким логином уже существует");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        User userForSave = convertFromRequestToEntity(userRequestDto);
        userForSave.setTasksAsAuthor(userFromDB.getTasksAsAuthor());
        userForSave.setTasksAsExecutor(userFromDB.getTasksAsExecutor());

        User updatedUser = userRepository.save(userForSave);
        return convertFromEntityToResponse(updatedUser);
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        logger.info(String.format("Удаление Пользователя с id #%d", id));

        User user = userRepository
                .findById(id)
                .orElseThrow(() -> {
                    UserNotFoundException exception = new UserNotFoundException(
                            String.format("Пользователь с id #%d не существует. Удаление невозможно", id)
                    );
                    logger.error(exception.getMessage(), exception);
                    throw exception;
                });

        userRepository.delete(user);
    }

    private User convertFromRequestToEntity(UserRequestDto requestDto) {
        return modelMapper.map(requestDto, User.class);
    }

    private UserResponseDto convertFromEntityToResponse(User user) {
        return modelMapper.map(user, UserResponseDto.class);
    }
}
