package com.github.vladioeroonda.tasktracker.dto.response;

import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Сущность Проекта(ответ)")
public class ProjectResponseDto {
    @Schema(description = "Id Проекта")
    private Long id;
    @Schema(description = "Название Проекта")
    private String name;
    @Schema(description = "Статус Проекта")
    private ProjectStatus status;
    @Schema(description = "Заказчик Проекта")
    private UserResponseDto customer;
    @Schema(description = "Цена разработки Проекта")
    private BigDecimal price;

    public ProjectResponseDto() {
    }

    public ProjectResponseDto(
            Long id,
            String name,
            ProjectStatus status,
            UserResponseDto customer,
            BigDecimal price
    ) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.customer = customer;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public UserResponseDto getCustomer() {
        return customer;
    }

    public void setCustomer(UserResponseDto customer) {
        this.customer = customer;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
