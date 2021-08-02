package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;

public interface ReleaseManagementService {
    /**
     * Подсчет количества задач, не завершившихся в заданный через ID Релиз
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     * @return int - кол-во Задач данного Релиза у которых статус, отличный от "завершен"
     */
    int countUnfinishedTasksByReleaseId(Long id);

    /**
     * Закрытие Релиза
     *
     * @param requestDto (ReleaseClosingRequestDto), представляет собой обёртку над запросом на закрытие Релиза
     * @return ReleaseResponseDto - объект-обёртку закрытого Релиза
     */
    ReleaseResponseDto closeRelease(ReleaseClosingRequestDto requestDto);
}
