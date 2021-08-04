package com.github.vladioeroonda.tasktracker.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Dto для закрытия Релиза(запрос)")
public class ReleaseClosingRequestDto {
    @Schema(description = "Id Проекта")
    private Long id;
    @Schema(description = "Время закрытия")
    private LocalDateTime finishTime;

    public ReleaseClosingRequestDto() {
    }

    public ReleaseClosingRequestDto(Long id, LocalDateTime finishTime) {
        this.id = id;
        this.finishTime = finishTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }
}
