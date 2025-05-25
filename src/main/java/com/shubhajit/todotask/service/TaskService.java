package com.shubhajit.todotask.service;

import com.shubhajit.todotask.model.Task;
import com.shubhajit.todotask.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        log.debug("Retrieving all tasks from repository");
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        log.debug("Retrieving task by id: {}", id);
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        if (task.getId() == null) {
            log.info("Saving new task: {}", task.getTitle());
        } else {
            log.info("Updating task with id: {}", task.getId());
        }
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        log.info("Deleting task with id: {}", id);
        taskRepository.deleteById(id);
    }
}
