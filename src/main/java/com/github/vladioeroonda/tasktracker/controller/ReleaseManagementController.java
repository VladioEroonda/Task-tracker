package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ReleaseClosingRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ReleaseResponseDto;
import com.github.vladioeroonda.tasktracker.service.ReleaseManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление Релизом", description = "Отвечает за управление Релизом")
@RestController
@RequestMapping("/api/tracker/release/management")
public class ReleaseManagementController {
    private static final Logger logger = LoggerFactory.getLogger(ReleaseManagementController.class);

    private final ReleaseManagementService releaseManagementService;

    public ReleaseManagementController(ReleaseManagementService releaseManagementService) {
        this.releaseManagementService = releaseManagementService;
    }

    @Operation(summary = "Подсчет количества задач, не завершившихся в заданный релиз")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> countUnfinishedTasksByReleaseId(@PathVariable Long id) {
        logger.info("GET /api/tracker/release/management/{id}");
        int count = releaseManagementService.countUnfinishedTasksByReleaseId(id);
        return ResponseEntity.ok().body(String.format("В релиз с id #%d не завершатся %d задач(и)", id, count));
    }

    @Operation(summary = "Изменение/закрытие релиза")
    @PatchMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ReleaseResponseDto> closeRelease(@RequestBody ReleaseClosingRequestDto requestDto) {
        logger.info("PATCH /api/tracker/release/management");
        ReleaseResponseDto release = releaseManagementService.closeRelease(requestDto);
        return ResponseEntity.ok().body(release);
    }
}
