package com.shubhajit.todotask.service;

import com.shubhajit.todotask.entity.Task;
import com.shubhajit.todotask.exception.TaskAlreadyExistsException;
import com.shubhajit.todotask.exception.TaskNotFoundException;
import com.shubhajit.todotask.mapper.TaskMapper;
import com.shubhajit.todotask.model.TaskDTO;
import com.shubhajit.todotask.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing tasks.
 * Handles business logic and repository interaction.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    /**
     * Retrieve all tasks.
     *
     * @return list of TaskDTOs
     */
    public List<TaskDTO> getAllTasks() {
        log.debug("Retrieving all tasks from repository");
        return taskRepository.findAll().stream().map(TaskMapper::toDTO).toList();
    }

    /**
     * Retrieve a task by its ID.
     *
     * @param id the task ID
     * @return TaskDTO if found
     * @throws TaskNotFoundException if not found
     */
    public TaskDTO getTaskById(Long id) {
        log.debug("Retrieving task by id: {}", id);
        return taskRepository.findById(id)
                .map(TaskMapper::toDTO)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + id));
    }

    /**
     * Save or update a task.
     *
     * @param dto the TaskDTO to save
     * @return the saved TaskDTO
     * @throws TaskAlreadyExistsException if duplicate title on create
     * @throws TaskNotFoundException      if updating non-existent task
     */
    public TaskDTO saveTask(TaskDTO dto) {
        Task entity = TaskMapper.toEntity(dto);
        if (entity.getId() == null) {
            // Check for duplicate by title (example logic, adjust as needed)
            boolean exists = taskRepository.findAll().stream()
                    .anyMatch(t -> t.getTitle().equalsIgnoreCase(entity.getTitle()));
            if (exists) {
                throw new TaskAlreadyExistsException("Task already exists with title: " + entity.getTitle());
            }
            log.info("Saving new task: {}", entity.getTitle());
        } else {
            // For update, check if task exists
            if (!taskRepository.existsById(entity.getId())) {
                throw new TaskNotFoundException("Task not found with id: " + entity.getId());
            }
            log.info("Updating task with id: {}", entity.getId());
        }
        Task saved = taskRepository.save(entity);
        return TaskMapper.toDTO(saved);
    }

    /**
     * Delete a task by its ID.
     *
     * @param id the task ID
     * @return true if deleted successfully
     * @throws TaskNotFoundException if task not found
     */
    public boolean deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task not found with id: " + id);
        }
        log.info("Deleting task with id: {}", id);
        taskRepository.deleteById(id);
        return true;
    }
}
