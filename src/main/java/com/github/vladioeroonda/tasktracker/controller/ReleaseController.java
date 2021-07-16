package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ReleaseNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/tracker")
public class ReleaseController {

    private final ModelMapper modelMapper;

    @Autowired
    public ReleaseController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/releases")
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

    @GetMapping(value = "/releases/{id}")
    public ResponseEntity<ReleaseResponseDto> getReleaseById(@PathVariable Long id) {

        ReleaseResponseDto release = new ReleaseResponseDto(
                1L,
                "0.0.1",
                LocalDateTime.now()
        );

        if (release == null) {
            throw new ReleaseNotFoundException("Релиза с id #" + id + " не существует");
        }

        return new ResponseEntity<>(release, HttpStatus.OK);
    }

    @PostMapping(value = "/releases")
    public ResponseEntity<ReleaseResponseDto> addNewRelease(@RequestBody ReleaseRequestDto requestDto) {
        ReleaseResponseDto release = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(release, HttpStatus.CREATED);
    }

    @PutMapping(value = "/releases")
    public ResponseEntity<ReleaseResponseDto> editRelease(@RequestBody ReleaseRequestDto requestDto) {
        ReleaseResponseDto release = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(release, HttpStatus.OK);
    }

    @DeleteMapping(value = "/releases/{id}")
    public ResponseEntity<String> deleteReleaseById(@PathVariable Long id) {
        String info = "Релиз с id #" + id + " был успешно удалён";

        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    private ReleaseResponseDto convertFromRequestToResponseDto(ReleaseRequestDto requestDto) {
        return modelMapper.map(requestDto, ReleaseResponseDto.class);
    }

    @ExceptionHandler(ReleaseNotFoundException.class)
    public ResponseEntity handleNotFoundException(ReleaseNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
