package com.github.vladioeroonda.tasktracker.service;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;

/**
 * Интерфейс описывающий операции управления Релизом (подсчёт задач, закрытие)
 */
public interface ReleaseManagementService {
    /**
     * Подсчет количества задач, не завершившихся в заданный через ID Релиз
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * @param id (Long), представляет собой уникальный ID Релиза
     * @return int - кол-во Задач данного Релиза у которых статус, отличный от "завершен"
     */
    int countUnfinishedTasksByReleaseId(Long id);

    /**
     * Закрытие Релиза.
     * Все незакрытые задачи изменят свой статус на "CANCELLED", в качестве релиза у них будет указан тот,
     * который был на момент закрытия.
     *
     * Выбрасываемые исключения:
     *
     * <li> ReleaseNotFoundException:
     * - если Релиза с указанным ID нет в БД.
     *
     * <li> ReleaseClosingException:
     * - если время закрытия Релиза раньше, чем время создания.
     *
     *
     * @param requestDto (ReleaseClosingRequestDto), представляет собой обёртку над запросом на закрытие Релиза
     * @return ReleaseResponseDto - объект-обёртку закрытого Релиза
     */
    ReleaseResponseDto closeRelease(ReleaseClosingRequestDto requestDto);
}
