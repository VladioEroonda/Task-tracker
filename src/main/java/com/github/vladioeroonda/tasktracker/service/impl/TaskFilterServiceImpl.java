package com.github.vladioeroonda.tasktracker.service.impl;

import com.github.vladioeroonda.tasktracker.dto.response.TaskResponseDto;
import com.github.vladioeroonda.tasktracker.model.Task;
import com.github.vladioeroonda.tasktracker.model.TaskStatus;
import com.github.vladioeroonda.tasktracker.repository.TaskRepository;
import com.github.vladioeroonda.tasktracker.service.TaskFilterService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskFilterServiceImpl implements TaskFilterService {
    private static final Logger logger = LoggerFactory.getLogger(TaskFilterServiceImpl.class);

    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper;

    public TaskFilterServiceImpl(TaskRepository taskRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<TaskResponseDto> getFilteredTasks(
            String name,
            String description,
            TaskStatus status,
            String projectName,
            String releaseVersion,
            String authorName,
            String executorName
    ) {
        logger.info("Поиск задачи по фильтру: Название задачи:{}, Описание задачи: {}, Статус задачи: {}, " +
                        "Название проекта: {}, Версия релиза: {}, Имя автора задачи: {}, Имя исполнителя Задачи: {} ",
                name, description, status, projectName, releaseVersion, authorName, executorName);

        List<Task> filteredTasks = taskRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isBlank(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }

            if (description != null && !description.isEmpty()) {
                predicates.add(cb.like(root.get("description"), "%" + description + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (projectName != null && !projectName.isEmpty()) {
                predicates.add(cb.like(root.join("project").get("name"), "%" + projectName + "%"));
            }

            if (releaseVersion != null && !releaseVersion.isEmpty()) {
                predicates.add(cb.equal(root.join("release").get("version"), releaseVersion));
            }

            if (authorName != null && !authorName.isEmpty()) {
                predicates.add(cb.like(root.join("author").get("name"), "%" + authorName + "%"));
            }

            if (executorName != null && !executorName.isEmpty()) {
                predicates.add(cb.like(root.join("executor").get("name"), "%" + executorName + "%"));
            }

            return query
                    .where(predicates.toArray(new Predicate[predicates.size()]))
                    .orderBy(cb.asc(root.get("name")))
                    .getRestriction();
        });

        return filteredTasks.stream()
                .map(entity -> convertFromEntityToResponse(entity))
                .collect(Collectors.toList()
                );
    }

    private TaskResponseDto convertFromEntityToResponse(Task task) {
        return modelMapper.map(task, TaskResponseDto.class);
    }
}
