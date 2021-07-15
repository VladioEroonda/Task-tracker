package com.github.vladioeroonda.tasktracker.dto.request;

public class UserRequestDto {
    private Long id;
    private String login;
    private String name;

    public UserRequestDto() {
    }

    public UserRequestDto(
            Long id,
            String login,
            String name
    ) {
        this.id = id;
        this.login = login;
        this.name = name;
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
}
