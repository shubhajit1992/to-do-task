package com.shubhajit.todotask.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shubhajit.todotask.model.Task;
import com.shubhajit.todotask.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // No need for MockitoAnnotations.openMocks(this) when using @ExtendWith
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
    }

    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Optional<Task> found = taskService.getTaskById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testSaveTask() {
        Task task = new Task();
        task.setTitle("New Task");
        when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(task);

        Task saved = taskService.saveTask(task);
        assertEquals("New Task", saved.getTitle());
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
