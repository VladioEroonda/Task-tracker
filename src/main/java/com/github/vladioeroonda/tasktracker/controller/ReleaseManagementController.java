package com.github.vladioeroonda.tasktracker.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Управление Релизом", description = "Отвечает за управление Релизом")
@RestController
@RequestMapping("/api/tracker/release/management")
public class ReleaseManagementController {

//    @Operation(summary = "Подсчет количества задач, не завершившихся в заданный релиз")
//    @PostMapping
//    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto requestDto) {
//        TaskResponseDto task = taskManagementService.changeTaskStatus(requestDto);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }

    //    @Operation(summary = "закрытие релиза")
//    @PutMapping
//    public ResponseEntity<TaskResponseDto> updateTask(@RequestBody TaskRequestDto requestDto) {
//        TaskResponseDto task = taskManagementService.changeTaskStatus(requestDto);
//        return new ResponseEntity<>(task, HttpStatus.OK);
//    }
}
