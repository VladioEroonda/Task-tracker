package com.github.vladioeroonda.tasktracker.controller;

import com.github.vladioeroonda.tasktracker.dto.request.ProjectRequestDto;
import com.github.vladioeroonda.tasktracker.dto.response.ProjectResponseDto;
import com.github.vladioeroonda.tasktracker.dto.response.UserResponseDto;
import com.github.vladioeroonda.tasktracker.exception.ProjectNotFoundException;
import com.github.vladioeroonda.tasktracker.model.ProjectStatus;
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

import java.util.List;

@RestController
@RequestMapping("/api/tracker")
public class ProjectController {

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/projects")
    public ResponseEntity<List<ProjectResponseDto>> getAllProjects() {
        ProjectResponseDto project = new ProjectResponseDto(
                1L,
                "First one",
                ProjectStatus.IN_PROGRESS,
                new UserResponseDto(1L, "ivan123", "Ivan Ivanov")
        );

        List<ProjectResponseDto> projects = List.of(project);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping(value = "/projects/{id}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long id) {
        //ProjectResponseDto project = projectService.getById(id);

        ProjectResponseDto project = new ProjectResponseDto(
                1L,
                "First one",
                ProjectStatus.IN_PROGRESS,
                new UserResponseDto(1L, "ivan123", "Ivan Ivanov")
        );

        if (project == null) {
            throw new ProjectNotFoundException("Проекта с id #" + id + " не существует");
        }

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @PostMapping(value = "/projects")
    public ResponseEntity<ProjectResponseDto> addNewProject(@RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto project = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    @PutMapping(value = "/projects")
    public ResponseEntity<ProjectResponseDto> editProject(@RequestBody ProjectRequestDto requestDto) {
        ProjectResponseDto project = convertFromRequestToResponseDto(requestDto);

        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @DeleteMapping(value = "/projects/{id}")
    public ResponseEntity<String> deleteProjectById(@PathVariable Long id) {
        //projectService.deleteById(id);
        String info = "Проект с id #" + id + " был успешно удалён";
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

    private ProjectResponseDto convertFromRequestToResponseDto(ProjectRequestDto requestDto) {
        return modelMapper.map(requestDto, ProjectResponseDto.class);
    }

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity handleNotFoundException(ProjectNotFoundException e) {
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
