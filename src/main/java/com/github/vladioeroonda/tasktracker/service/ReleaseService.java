package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.model.Release;

import java.util.List;

/**
 * Интерфейс операций с Релизом
 */
public interface ReleaseService {

    /**
     * Получение списка всех существующих в БД Релизов
     *
     * @return Список Релизов (обёртку)
     */
    List<ReleaseResponseDto> getAllReleases();

    /**
     * Получение конкретного Релиза по его ID и возвращение его в виде обёртки.
     * Используется для предоставления запрашиваемых данных пользователю.
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     * @return ReleaseResponseDto - объект Релиза (обёртка)
     */
    ReleaseResponseDto getReleaseByIdAndReturnResponseDto(Long id);

    /**
     * Получение конкретного Релиза по его ID и возвращение его без обёрток.
     * Используется для вызова в сервисах (не относящихся к Релизу напрямую)
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     * @return Release - объект Релиза
     */
    Release getReleaseByIdAndReturnEntity(Long id);

    /**
     * Проверка на наличие конкретного Релиза по его ID.
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     */
    void checkReleaseExistsById(Long id);

    /**
     * Добавление нового Релиза
     *
     * @param releaseRequestDto (ReleaseRequestDto), представляет собой новый Релиз (обёртку)
     * @return ReleaseRequestDto - объект (обёртка) добавленного Релиза
     */
    ReleaseResponseDto addRelease(ReleaseRequestDto releaseRequestDto);

    /**
     * Обновление уже существующего Релиза
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * <li> ReleaseBadDataException
     * - если пользователь пытается закрыть Релиз(установить дату завершения)
     *
     * @param releaseRequestDto (ReleaseRequestDto), представляет собой обновляемый Релиз (обёртку)
     * @return ReleaseRequestDto - объект (обёртка) обновлённого Релиза
     */
    ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto);

    /**
     * Удаление конкретного Релиза по его ID
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     */
    void deleteRelease(Long id);

    /**
     * Получение всех незакрытых Релизов по ID Проекта
     *
     * Выбрасываемые исключения:
     *
     * <li> ProjectNotFoundException:
     * - если Проекта с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой ID Проекта
     */
    List<Release> getAllNotClosedReleasesByProjectId(Long id);
}
