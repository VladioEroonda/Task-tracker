package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseBadDataException;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Release;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.service.ProjectService;
import com.github.vladioeroonda.tasktracker.service.ReleaseService;
import com.github.vladioeroonda.tasktracker.util.Translator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReleaseServiceImpl implements ReleaseService {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseServiceImpl.class);

    private final ReleaseRepository releaseRepository;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    public ReleaseServiceImpl(
            ReleaseRepository releaseRepository,
            ProjectService projectService,
            ModelMapper modelMapper
    ) {
        this.releaseRepository = releaseRepository;
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<ReleaseResponseDto> getAllReleases() {
        logger.info("Получение списка всех Релизов");

        List<Release> releases = releaseRepository.findAll();
        return releases.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public ReleaseResponseDto getReleaseByIdAndReturnResponseDto(Long id) {
        logger.info(String.format("Получение Релиза с id #%d", id));

        Release release = releaseRepository
                .findById(id)
                .orElseThrow(() -> {
                    ReleaseNotFoundException exception =
                            new ReleaseNotFoundException(String.format(Translator.toLocale("exception.release.not-found-by-id"), id));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        return convertFromEntityToResponse(release);
    }

    @Transactional
    @Override
    public Release getReleaseByIdAndReturnEntity(Long id) {
        return releaseRepository
                .findById(id)
                .orElseThrow(() -> {
                    ReleaseNotFoundException exception =
                            new ReleaseNotFoundException(String.format(Translator.toLocale("exception.release.not-found-by-id"), id));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });
    }

    @Transactional
    @Override
    public void checkReleaseExistsById(Long id) {
        if (releaseRepository.findById(id).isEmpty()) {
            ReleaseNotFoundException exception =
                    new ReleaseNotFoundException(String.format(Translator.toLocale("exception.release.not-found-by-id"), id));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }
    }

    @Transactional
    @Override
    public ReleaseResponseDto addRelease(ReleaseRequestDto releaseRequestDto) {
        logger.info("Добавление нового Релиза");

        Release releaseForSave = convertFromRequestToEntity(releaseRequestDto);
        releaseForSave.setId(null);
        releaseForSave.setStartTime(LocalDateTime.now());
        releaseForSave.setFinishTime(null);

        Release savedRelease = releaseRepository.save(releaseForSave);
        return convertFromEntityToResponse(savedRelease);
    }

    @Transactional
    @Override
    public ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto) {
        logger.info(String.format("Обновление Релиза с id #%d", releaseRequestDto.getId()));

        Release releaseFromBD = releaseRepository
                .findById(releaseRequestDto.getId())
                .orElseThrow(() -> {
                    ReleaseNotFoundException exception =
                            new ReleaseNotFoundException(
                                    String.format(Translator.toLocale("exception.release.not-found-by-id"), releaseRequestDto.getId())
                            );
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        if (releaseRequestDto.getFinishTime() != null) {
            ReleaseBadDataException exception =
                    new ReleaseBadDataException(Translator.toLocale("exception.release.unable-to-close-here"));
            logger.error(exception.getMessage(), exception);
            throw exception;
        }

        Release releaseForSave = convertFromRequestToEntity(releaseRequestDto);
        releaseForSave.setTasks(releaseFromBD.getTasks());

        Release updatedRelease = releaseRepository.save(releaseForSave);
        return convertFromEntityToResponse(updatedRelease);

    }

    @Transactional
    @Override
    public void deleteRelease(Long id) {
        logger.info(String.format("Удаление Релиза с id #%d", id));

        Release release = releaseRepository
                .findById(id)
                .orElseThrow(() -> {
                    ReleaseNotFoundException exception =
                            new ReleaseNotFoundException(String.format(Translator.toLocale("exception.release.not-found-by-id"), id));
                    logger.error(exception.getMessage(), exception);
                    return exception;
                });

        releaseRepository.delete(release);
    }

    @Transactional
    @Override
    public List<Release> getAllNotClosedReleasesByProjectId(Long id) {
        projectService.checkProjectExistsById(id);
        return releaseRepository.getAllNotClosedReleasesByProjectId(id);
    }

    private Release convertFromRequestToEntity(ReleaseRequestDto requestDto) {
        return modelMapper.map(requestDto, Release.class);
    }

    private ReleaseResponseDto convertFromEntityToResponse(Release release) {
        return modelMapper.map(release, ReleaseResponseDto.class);
    }
}
