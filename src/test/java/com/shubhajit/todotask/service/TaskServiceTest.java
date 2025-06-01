package com.shubhajit.todotask.service;

import static com.shubhajit.todotask.mapper.TaskMapper.toDTO;
import static com.shubhajit.todotask.mapper.TaskMapper.toEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.shubhajit.todotask.entity.Task;
import com.shubhajit.todotask.exception.TaskNotFoundException;
import com.shubhajit.todotask.model.TaskDTO;
import com.shubhajit.todotask.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        when(taskRepository.findAll()).thenReturn(Arrays.asList(
                Task.builder().id(1L).title("Task 1").description("desc").completed(false).build(),
                Task.builder().id(2L).title("Task 2").description("desc").completed(false).build()
        ));
        List<TaskDTO> tasks = taskService.getAllTasks();
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
    }

    @Test
    void testGetTaskById() {
        Task entity = Task.builder().id(1L).title("Task 1").description("desc").completed(false).build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(entity));
        TaskDTO found = taskService.getTaskById(1L);
        assertEquals(1L, found.getId());
    }

    @Test
    void testGetTaskById_notFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(2L);
        });
        assertEquals("Task not found with id: 2", exception.getMessage());
    }

    @Test
    void testSaveTask() {
        TaskDTO dto = TaskDTO.builder().id(null).title("New Task").description("desc").completed(false).build();
        Task savedEntity = Task.builder().id(1L).title("New Task").description("desc").completed(false).build();
        when(taskRepository.save(any(Task.class))).thenReturn(savedEntity);
        TaskDTO saved = taskService.saveTask(dto);
        assertEquals("New Task", saved.getTitle());
        assertEquals(1L, saved.getId());
    }

    @Test
    void testSaveTask_AlreadyExists() {
        TaskDTO dto = TaskDTO.builder().id(null).title("Duplicate").description("desc").completed(false).build();
        Task existing = Task.builder().id(2L).title("Duplicate").description("desc").completed(false).build();
        when(taskRepository.findAll()).thenReturn(List.of(existing));
        com.shubhajit.todotask.exception.TaskAlreadyExistsException exception = assertThrows(com.shubhajit.todotask.exception.TaskAlreadyExistsException.class, () -> {
            taskService.saveTask(dto);
        });
        assertEquals("Task already exists with title: Duplicate", exception.getMessage());
    }

    @Test
    void testSaveTask_UpdateNotFound() {
        TaskDTO dto = TaskDTO.builder().id(99L).title("Update").description("desc").completed(false).build();
        when(taskRepository.existsById(99L)).thenReturn(false);
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.saveTask(dto);
        });
        assertEquals("Task not found with id: 99", exception.getMessage());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);
        boolean deleted = taskService.deleteTask(1L);
        assertTrue(deleted);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTask_notFound() {
        when(taskRepository.existsById(2L)).thenReturn(false);
        TaskNotFoundException exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(2L);
        });
        assertEquals("Task not found with id: 2", exception.getMessage());
    }

    @Test
    void testToDTO_NullInput() {
        assertNull(toDTO(null));
    }

    @Test
    void testToEntity_NullInput() {
        assertNull(toEntity(null));
    }
}
