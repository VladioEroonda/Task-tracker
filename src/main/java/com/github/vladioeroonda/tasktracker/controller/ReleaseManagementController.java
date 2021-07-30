package com.github.vladioeroonda.tasktracker.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление Релизом", description = "Отвечает за управление Релизом")
@RestController
@RequestMapping("/api/tracker/release/management")
public class ReleaseManagementController {

}
