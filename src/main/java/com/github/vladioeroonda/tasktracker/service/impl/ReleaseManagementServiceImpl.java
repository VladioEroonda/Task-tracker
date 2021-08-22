package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseClosingException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Release;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.service.ReleaseManagementService;
import com.github.vladioeroonda.tasktracker.service.TaskService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReleaseManagementServiceImpl implements ReleaseManagementService {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseManagementServiceImpl.class);

    private final ReleaseRepository releaseRepository;
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    public ReleaseManagementServiceImpl(ReleaseRepository releaseRepository, TaskService taskService, ModelMapper modelMapper) {
        this.releaseRepository = releaseRepository;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public int countUnfinishedTasksByReleaseId(Long id) {
        logger.info(String.format("Подсчёт незавершённых задач Релиза с id #%d", id));

        if (!releaseRepository.existsById(id)) {
            ReleaseNotFoundException exception =
                    new ReleaseNotFoundException(String.format("Релиз с id #%s не найден", id));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
        return taskService.countUnfinishedTasksByReleaseId(id);
    }

    @Transactional
    @Override
    public ReleaseResponseDto closeRelease(ReleaseClosingRequestDto requestDto) {
        logger.info(String.format("Закрытие Релиза с id #%d", requestDto.getId()));

        Release release = releaseRepository
                .findById(requestDto.getId())
                .orElseThrow(() -> {
                    ReleaseNotFoundException exception =
                            new ReleaseNotFoundException(String.format("Релиз с id #%d не существует.", requestDto.getId()));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        if (
                (requestDto.getFinishTime() != null && release.getStartTime() != null) &&
                        (requestDto.getFinishTime().isBefore(release.getStartTime()))
        ) {
            ReleaseClosingException exception =
                    new ReleaseClosingException("Указанное время завершения Релиза раньше его начала");
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        release.setFinishTime(requestDto.getFinishTime());

        taskService.setAllTasksCancelled(requestDto.getId());
        return convertFromEntityToResponse(release);
    }

    private ReleaseResponseDto convertFromEntityToResponse(Release release) {
        return modelMapper.map(release, ReleaseResponseDto.class);
    }
}
