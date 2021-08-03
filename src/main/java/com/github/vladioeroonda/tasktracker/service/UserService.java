package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;

import java.util.List;

/**
 * Интерфейс (сервисного слоя) операций с Пользователем
 */
public interface UserService {

    /**
     * Получение списка всех существующих Пользователей
     * @return List Пользователей (обёртку)
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Получение конкретного Пользователя по его ID
     * @param id (Long), представляет собой уникальный ID Пользователя
     * @return UserResponseDto - объект Пользователя (обёртка)
     */
    UserResponseDto getUserById(Long id);

    /**
     * Добавление нового Пользователя
     * @param userRequestDto (UserRequestDto), представляет собой нового Пользователя
     * @return UserRequestDto - объект (обёртка) добавленного Пользователя
     */
    UserResponseDto addUser(UserRequestDto userRequestDto);

    /**
     * Обновление уже существующего Пользователя
     * @param userRequestDto (UserRequestDto), представляет собой обновляемого Пользователя
     * @return UserRequestDto - объект (обёртка) обновлённого Пользователя
     */
    UserResponseDto updateUser(UserRequestDto userRequestDto);

    /**
     * Удаление конкретного Пользователя по его ID
     * @param id (Long), представляет собой уникальный ID Пользователя
     */
    void deleteUser(Long id);
}
