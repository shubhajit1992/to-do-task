package com.shubhajit.todotask;

import static org.assertj.core.api.Assertions.assertThat;

import com.shubhajit.todotask.model.TaskDTO;
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
        TaskDTO task = TaskDTO.builder()
                .id(null)
                .title("Integration Task")
                .description("Integration Description")
                .completed(false)
                .build();
        ResponseEntity<TaskDTO> postResponse = restTemplate.postForEntity("/api/tasks", task, TaskDTO.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TaskDTO created = postResponse.getBody();
        assertThat(created).isNotNull();
        if (created == null) return;
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Integration Task");

        ResponseEntity<TaskDTO> getResponse = restTemplate.getForEntity("/api/tasks/" + created.getId(), TaskDTO.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(getResponse.getBody()).getTitle()).isEqualTo("Integration Task");
    }

    @Test
    void testUpdateTask() {
        TaskDTO task = TaskDTO.builder()
                .id(null)
                .title("Old Title")
                .description("Old Desc")
                .completed(false)
                .build();
        TaskDTO saved = restTemplate.postForObject("/api/tasks", task, TaskDTO.class);
        saved.setTitle("Updated Title");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskDTO> entity = new HttpEntity<>(saved, headers);
        ResponseEntity<TaskDTO> response = restTemplate.exchange("/api/tasks/" + saved.getId(), HttpMethod.PUT, entity, TaskDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testDeleteTask() {
        TaskDTO task = TaskDTO.builder()
                .id(null)
                .title("To Delete")
                .description("To Delete Desc")
                .completed(false)
                .build();
        TaskDTO saved = restTemplate.postForObject("/api/tasks", task, TaskDTO.class);
        restTemplate.delete("/api/tasks/" + saved.getId());
        ResponseEntity<TaskDTO> response = restTemplate.getForEntity("/api/tasks/" + saved.getId(), TaskDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
