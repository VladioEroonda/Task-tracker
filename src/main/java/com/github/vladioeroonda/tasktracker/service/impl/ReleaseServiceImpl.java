package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.model.Release;
import com.github.vladioeroonda.tasktracker.repository.ReleaseRepository;
import com.github.vladioeroonda.tasktracker.service.ReleaseService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReleaseServiceImpl implements ReleaseService {

    private final ReleaseRepository releaseRepository;
    private final ModelMapper modelMapper;

    public ReleaseServiceImpl(ReleaseRepository releaseRepository, ModelMapper modelMapper) {
        this.releaseRepository = releaseRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public List<ReleaseResponseDto> getAllReleases() {
        List<Release> releases = releaseRepository.findAll();
        return releases.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public ReleaseResponseDto getReleaseById(Long id) {
        Release release = releaseRepository
                .findById(id)
                .orElseThrow(
                        () -> new ReleaseNotFoundException(String.format("Релиз с id #%s не существует", id))
                );

        return convertFromEntityToResponse(release);
    }

    @Transactional
    @Override
    public ReleaseResponseDto addRelease(ReleaseRequestDto releaseRequestDto) {
        Release releaseForSave = convertFromRequestToEntity(releaseRequestDto);
        releaseForSave.setId(null);
        releaseForSave.setStartTime(LocalDateTime.now());

        Release savedRelease = releaseRepository.save(releaseForSave);
        return convertFromEntityToResponse(savedRelease);
    }

    @Transactional
    @Override
    public ReleaseResponseDto updateRelease(ReleaseRequestDto releaseRequestDto) {
        Release releaseFromBD = releaseRepository
                .findById(releaseRequestDto.getId())
                .orElseThrow(
                        () -> new ReleaseNotFoundException(
                                String.format("Релиз с id #%s не существует", releaseRequestDto.getId())
                        )
                );

        Release releaseForSave = convertFromRequestToEntity(releaseRequestDto);
        releaseForSave.setTasks(releaseFromBD.getTasks());

        Release updatedRelease = releaseRepository.save(releaseForSave);
        return convertFromEntityToResponse(updatedRelease);

    }

    @Transactional
    @Override
    public void deleteRelease(Long id) {
        Release release = releaseRepository
                .findById(id)
                .orElseThrow(
                        () -> new ReleaseNotFoundException(
                                String.format("Релиз с id #%s не существует. Удаление невозможно", id)
                        )
                );

        releaseRepository.delete(release);
    }

    private Release convertFromRequestToEntity(ReleaseRequestDto requestDto) {
        return modelMapper.map(requestDto, Release.class);
    }

    private ReleaseResponseDto convertFromEntityToResponse(Release release) {
        return modelMapper.map(release, ReleaseResponseDto.class);
    }
}
