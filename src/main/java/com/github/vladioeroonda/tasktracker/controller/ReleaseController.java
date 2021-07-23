package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import com.github.vladioeroonda.tasktracker.service.ReleaseService;
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

import java.util.List;

@Tag(name = "Релиз", description = "Отвечает за CRUD операции, связанные с Релизом")
@RestController
@RequestMapping("/api/tracker/release")
public class ReleaseController {

    private final ModelMapper modelMapper;
    private final ReleaseService releaseService;

    public ReleaseController(ModelMapper modelMapper, ReleaseService releaseService) {
        this.modelMapper = modelMapper;
        this.releaseService = releaseService;
    }

    @Operation(summary = "Получение списка всех Релизов")
    @GetMapping
    public ResponseEntity<List<ReleaseResponseDto>> getAllReleases() {
        List<ReleaseResponseDto> releases = releaseService.getAllReleases();
        return new ResponseEntity<>(releases, HttpStatus.OK);
    }

    @Operation(summary = "Получение конкретного Релиза по его id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ReleaseResponseDto> getReleaseById(@PathVariable Long id) {
        ReleaseResponseDto release = releaseService.getReleaseById(id);
        return new ResponseEntity<>(release, HttpStatus.OK);
    }

    @Operation(summary = "Добавление нового Релиза")
    @PostMapping
    public ResponseEntity<ReleaseResponseDto> addNewRelease(@RequestBody ReleaseRequestDto requestDto) {
        ReleaseResponseDto release = releaseService.addRelease(requestDto);
        return new ResponseEntity<>(release, HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение Релиза")
    @PutMapping
    public ResponseEntity<ReleaseResponseDto> updateRelease(@RequestBody ReleaseRequestDto requestDto) {
        ReleaseResponseDto release = releaseService.updateRelease(requestDto);
        return new ResponseEntity<>(release, HttpStatus.OK);
    }

    @Operation(summary = "Удаление конкретного Релиза по его id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteReleaseById(@PathVariable Long id) {
        releaseService.deleteRelease(id);
        return new ResponseEntity<>(
                String.format("Релиз с id #%s был успешно удалён", id),
                HttpStatus.OK
        );
    }

    @ExceptionHandler(ReleaseNotFoundException.class)
    public ResponseEntity handleNotFoundException(ReleaseNotFoundException e) {
        return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
