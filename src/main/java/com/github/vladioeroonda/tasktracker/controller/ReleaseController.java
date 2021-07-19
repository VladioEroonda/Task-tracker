package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Релиз", description = "Отвечает за CRUD операции, связанные с Релизом")
@RestController
@RequestMapping("/api/tracker/release")
public class ReleaseController {

    private final ModelMapper modelMapper;

    public ReleaseController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Получение списка всех Релизов")
    @GetMapping
    public ResponseEntity<List<ReleaseResponseDto>> getAllReleases() {
        ReleaseResponseDto release1 = new ReleaseResponseDto(
                1L,
                "0.0.1",
                LocalDateTime.now()
        );
        ReleaseResponseDto release2 = new ReleaseResponseDto(
                2L,
                "0.1.1",
                LocalDateTime.now()
        );

        List<ReleaseResponseDto> releases = List.of(release1, release2);
        return new ResponseEntity<>(releases, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретного Релиза по его id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ReleaseResponseDto> getReleaseById(@PathVariable Long id) {

        ReleaseResponseDto release = new ReleaseResponseDto(
                1L,
                "0.0.1",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(release, HttpStatus.OK);
    }

    @Operation(summary = "Добавление нового Релиза")
    @PostMapping
    public ResponseEntity<ReleaseResponseDto> addNewRelease(@RequestBody ReleaseRequestDto requestDto) {
        ReleaseResponseDto release = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(release, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Релиза")
    @PutMapping
    public ResponseEntity<ReleaseResponseDto> editRelease(@RequestBody ReleaseRequestDto requestDto) {
        ReleaseResponseDto release = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(release, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретного Релиза по его id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteReleaseById(@PathVariable Long id) {

        return new ResponseEntity<>(
                String.format("Релиз с id #%s был успешно удалён", id),
                HttpStatus.OK
        );
    }

    private ReleaseResponseDto convertFromRequestToResponseDto(ReleaseRequestDto requestDto) {
        return modelMapper.map(requestDto, ReleaseResponseDto.class);
    }

    @ExceptionHandler(ReleaseNotFoundException.class)
    public ResponseEntity handleNotFoundException(ReleaseNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
