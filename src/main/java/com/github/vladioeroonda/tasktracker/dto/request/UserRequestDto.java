package com.github.vladioeroonda.tasktracker.dto.request;

import com.github.vladioeroonda.tasktracker.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "Сущность Пользователя(запрос)")
public class UserRequestDto {
    @Schema(description = "ID Пользователя")
    private Long id;
    @Schema(description = "Логин Пользователя")
    private String login;
    @Schema(description = "Пароль Пользователя")
    private String password;
    @Schema(description = "ФИО Пользователя")
    private String name;
    @Schema(description = "Роли Пользователя")
    private Set<Role> roles;
    @Schema(description = "Банковский счёт Пользователя")
    private String bankAccountId;

    public UserRequestDto() {
    }

    public UserRequestDto(Long id) {
        this.id = id;
    }

    public UserRequestDto(
            Long id,
            String login,
            String password,
            String name,
            Set<Role> roles,
            String bankAccountId
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.roles = roles;
        this.bankAccountId = bankAccountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }
}
