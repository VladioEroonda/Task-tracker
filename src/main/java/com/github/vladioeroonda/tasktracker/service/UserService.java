package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.UserRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.model.User;

import java.util.List;

/**
 * Интерфейс операций с Пользователем
 */
public interface UserService {

    /**
     * Получение списка всех существующих в БД Пользователей
     *
     * @return Список Пользователей (обёртку)
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Получение конкретного Пользователя по его ID и возвращение его в виде обёртки.
     * Используется для предоставления запрашиваемых данных пользователю.
     *
     * Выбрасываемые исключения:
     *
     * <li> UserNotFoundException:
     * - если Пользователя с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Пользователя
     * @return UserResponseDto - объект Пользователя (обёртка)
     */
    UserResponseDto getUserByIdAndReturnResponseDto(Long id);

    /**
     * Получение конкретного Пользователя по его ID/
     * Используется для вызова в сервисах (не относящихся к Пользователю напрямую)
     *
     * Выбрасываемые исключения:
     *
     * <li> UserNotFoundException:
     * - если Пользователя с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Пользователя
     * @return User - объект Пользователя
     */
    User getUserByIdAndReturnEntity(Long id);

    /**
     * Проверка на наличие конкретного Пользователя по его ID
     *
     * Выбрасываемые исключения:
     *
     * <li> UserNotFoundException:
     * - если Пользователя с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Пользователя
     */
    void checkUserExistsById(Long id);

    /**
     * Добавление нового Пользователя
     *
     * Выбрасываемые исключения:
     *
     * <li> UserBadDataException:
     * - если Пользователь с указанным логином уже есть в БД.
     *
     * @param userRequestDto (UserRequestDto), представляет собой нового Пользователя
     * @return UserRequestDto - объект (обёртка) добавленного Пользователя
     */
    UserResponseDto addUser(UserRequestDto userRequestDto);

    /**
     * Обновление уже существующего Пользователя
     *
     * Выбрасываемые исключения:
     *
     * <li> UserNotFoundException:
     * - если Пользователя с указанным ID нет в БД.
     *
     * <li> UserBadDataException:
     * - если Пользователь с указанным логином уже есть в БД.
     *
     *
     * @param userRequestDto (UserRequestDto), представляет собой обновляемого Пользователя
     * @return UserRequestDto - объект (обёртка) обновлённого Пользователя
     */
    UserResponseDto updateUser(UserRequestDto userRequestDto);

    /**
     * Удаление конкретного Пользователя по его ID
     *
     * Выбрасываемые исключения:
     *
     * <li> UserNotFoundException:
     * - если Пользователя с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Пользователя
     */
    void deleteUser(Long id);
}
