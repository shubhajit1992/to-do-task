package com.shubhajit.todotask.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
import com.shubhajit.todotask.model.Task;
import com.shubhajit.todotask.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

@WebMvcTest
class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
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
    void testGetTaskById_found() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetTaskById_notFound() throws Exception {
        when(taskService.getTaskById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/tasks/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateTask() throws Exception {
        when(taskService.saveTask(any(Task.class))).thenReturn(task);
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(header().string("Location", "/api/tasks/1"));
    }

    @Test
    void testUpdateTask_found() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        when(taskService.saveTask(any(Task.class))).thenReturn(task);
        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateTask_notFound() throws Exception {
        when(taskService.getTaskById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/tasks/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTask_found() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(Optional.of(task));
        doNothing().when(taskService).deleteTask(1L);
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());
        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void testDeleteTask_notFound() throws Exception {
        when(taskService.getTaskById(2L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/tasks/2"))
                .andExpect(status().isNotFound());
    }
}
