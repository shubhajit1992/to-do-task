package com.shubhajit.todotask.mapper;

import com.shubhajit.todotask.entity.Task;
import com.shubhajit.todotask.model.TaskDTO;

/**
 * Utility class for mapping between Task entity and TaskDTO.
 */
public class TaskMapper {
    private TaskMapper() {}

    /**
     * Convert Task entity to TaskDTO.
     * @param task the Task entity
     * @return the TaskDTO
     */
    public static TaskDTO toDTO(Task task) {
        if (task == null) return null;
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .build();
    }

    /**
     * Convert TaskDTO to Task entity.
     * @param dto the TaskDTO
     * @return the Task entity
     */
    public static Task toEntity(TaskDTO dto) {
        if (dto == null) return null;
        return Task.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .completed(dto.isCompleted())
                .build();
    }
}
