package com.github.vladioeroonda.tasktracker.dto.request;

import java.time.LocalDateTime;

public class ReleaseRequestDto {
    private Long id;
    private String version;
    private LocalDateTime startTime;
    private LocalDateTime finishTime;

    public ReleaseRequestDto() {
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
