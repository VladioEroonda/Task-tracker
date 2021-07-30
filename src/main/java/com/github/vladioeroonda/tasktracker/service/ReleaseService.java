package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;

import java.util.List;

/**
 * Интерфейс (сервисного слоя) операций с Релизом
 */
public interface ReleaseService {

    /**
     * Получение списка всех существующих Релизов
     *
     * @return List Релизов (обёртку)
     */
    List<ReleaseResponseDto> getAllReleases();

    /**
     * Получение конкретного Релиза по его ID
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     * @return ReleaseResponseDto - объект Релиза (обёртка)
     */
    ReleaseResponseDto getReleaseById(Long id);

    /**
     * Добавление нового Релиза
     *
     * @param releaseRequestDto (ReleaseRequestDto), представляет собой новый Релиз
     * @return ReleaseRequestDto - объект (обёртка) добавленного Релиза
     */
    ReleaseResponseDto addRelease(ReleaseRequestDto releaseRequestDto);

    /**
     * Обновление уже существующего Релиза
     *
     * @param releaseRequestDto (ReleaseRequestDto), представляет собой обновляемый Релиз
     * @return ReleaseRequestDto - объект (обёртка) обновлённого Релиза
     */
    ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto);

    /**
     * Удаление конкретного Релиза по его ID
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     */
    void deleteRelease(Long id);
}
