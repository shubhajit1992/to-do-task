package com.shubhajit.todotask;

import static org.assertj.core.api.Assertions.assertThat;

import com.shubhajit.todotask.model.Task;
import com.shubhajit.todotask.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TaskIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    void testCreateAndGetTask() {
        Task task = new Task();
        task.setTitle("Integration Task");
        task.setDescription("Integration Description");
        task.setCompleted(false);

        ResponseEntity<Task> postResponse = restTemplate.postForEntity("/api/tasks", task, Task.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Task created = postResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Integration Task");

        ResponseEntity<Task> getResponse = restTemplate.getForEntity("/api/tasks/" + created.getId(), Task.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getTitle()).isEqualTo("Integration Task");
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setTitle("Old Title");
        task.setDescription("Old Desc");
        task.setCompleted(false);
        Task saved = taskRepository.save(task);

        saved.setTitle("Updated Title");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Task> entity = new HttpEntity<>(saved, headers);
        ResponseEntity<Task> response = restTemplate.exchange("/api/tasks/" + saved.getId(), HttpMethod.PUT, entity, Task.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setTitle("To Delete");
        task.setDescription("To Delete Desc");
        task.setCompleted(false);
        Task saved = taskRepository.save(task);

        restTemplate.delete("/api/tasks/" + saved.getId());
        ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/" + saved.getId(), Task.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
