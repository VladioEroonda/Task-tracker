package com.github.vladioeroonda.tasktracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Сущность Релиза(запрос)")
public class ReleaseRequestDto {
    @Schema(description = "ID Релиза")
    private Long id;
    @Schema(description = "Версия Релиза", example = "0.0.1")
    private String version;
    @Schema(description = "Время начала выполнения Релиза")
    private LocalDateTime startTime;
    @Schema(description = "Время завершения работ Релиза")
    private LocalDateTime finishTime;

    public ReleaseRequestDto() {
    }

    public ReleaseRequestDto(Long id, String version, LocalDateTime startTime) {
        this.id = id;
        this.version = version;
        this.startTime = startTime;
    }

    public ReleaseRequestDto(Long id) {
        this.id = id;
    }

    public ReleaseRequestDto(
            Long id,
            String version,
            LocalDateTime startTime,
            LocalDateTime finishTime
    ) {
        this.id = id;
        this.version = version;
        this.startTime = startTime;
        this.finishTime = finishTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }
}
