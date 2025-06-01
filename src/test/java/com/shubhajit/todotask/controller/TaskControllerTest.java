package com.shubhajit.todotask.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shubhajit.todotask.exception.TaskAlreadyExistsException;
import com.shubhajit.todotask.exception.TaskNotFoundException;
import com.shubhajit.todotask.model.TaskDTO;
import com.shubhajit.todotask.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

@WebMvcTest
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private TaskDTO task;

    @BeforeEach
    void setUp() {
        task = new TaskDTO();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
    }

    @Test
    void testGetAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void testGetTaskById_constraintViolation() throws Exception {
        mockMvc.perform(get("/api/tasks/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testGetTaskById_found() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(task);
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetTaskById_notFound() throws Exception {
        when(taskService.getTaskById(2L)).thenThrow(new TaskNotFoundException("Task not found with id: 2"));
        mockMvc.perform(get("/api/tasks/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidationOnCreate() throws Exception {
        TaskDTO invalidTask = new TaskDTO();
        invalidTask.setTitle(""); // Assuming title is required
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateTask() throws Exception {
        when(taskService.saveTask(any(TaskDTO.class))).thenReturn(task);
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(header().string("Location", "/api/tasks/1"));
    }

    @Test
    void testCreateTask_AlreadyExists() throws Exception {
        when(taskService.saveTask(any(TaskDTO.class)))
                .thenThrow(new TaskAlreadyExistsException("Task already exists with title: Test Task"));
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Task already exists with title: Test Task"));
    }

    @Test
    void testValidationOnUpdate() throws Exception {
        TaskDTO invalidTask = new TaskDTO();
        invalidTask.setTitle(""); // Assuming title is required
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTask)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTask() throws Exception {
        when(taskService.saveTask(any(TaskDTO.class))).thenReturn(task);
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).deleteTask(1L);
    }
}
