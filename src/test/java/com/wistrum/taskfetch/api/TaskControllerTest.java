package com.wistrum.taskfetch.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wistrum.taskfetch.api.controller.TaskController;
import com.wistrum.taskfetch.api.exception.IllegalInputException;
import com.wistrum.taskfetch.api.model.Task;
import com.wistrum.taskfetch.api.model.TaskStatus;
import com.wistrum.taskfetch.api.service.TaskService;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    private Task validTask;

    @BeforeEach
    void setUp() {
        validTask = new Task("Valid Task", "Valid Description", "PENDING");
        validTask.setId(1L);
    }

    // Utility method to convert objects to JSON
    private String asJsonString(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    // ------------------------------ GET /tests ------------------------------
    @Test
    void getAllTasks_ReturnsTasks() throws Exception {
        List<Task> tasks = List.of(validTask, new Task("Task 2", "Description 2", "COMPLETED"));
        Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/tasks"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].title", is("Valid Task")))
               .andExpect(jsonPath("$[1].status", is("COMPLETED")));
    }

    @Test
    void getAllTasks_NoTasks_ReturnsNoContent() throws Exception {
        Mockito.when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks"))
               .andExpect(status().isNoContent());
    }

    // ------------------------------ GET /tasks/{id} ------------------------------
    @Test
    void getTaskById_ValidId_ReturnsTask() throws Exception {
        Mockito.when(taskService.getTaskById(1L)).thenReturn(validTask);

        mockMvc.perform(get("/tasks/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title", is("Valid Task")))
               .andExpect(jsonPath("$.description", is("Valid Description")))
               .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void getTaskById_InvalidId_ReturnsNotFound() throws Exception {
        Mockito.when(taskService.getTaskById(999L)).thenThrow(new EntityNotFoundException("Task not found."));

        mockMvc.perform(get("/tasks/999"))
               .andExpect(status().isNotFound());
    }

    // ------------------------------ GET /tasks/status ------------------------------
    @Test
    void getTasksByStatus_ValidStatus_ReturnsTasks() throws Exception {
        List<Task> tasks = List.of(validTask);
        Mockito.when(taskService.getTasksByStatus(TaskStatus.PENDING)).thenReturn(tasks);

        mockMvc.perform(get("/tasks/status")
               .param("status", "PENDING"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].title", is("Valid Task")));
    }

    @Test
    void getTasksByStatus_InvalidStatus_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/tasks/status")
               .param("status", "INVALID_STATUS"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string(containsString("Invalid Status")));
    }

    @Test
    void getTasksByStatus_NoTasks_ReturnsNotFound() throws Exception {
        Mockito.when(taskService.getTasksByStatus(TaskStatus.PROCESSING)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/tasks/status")
               .param("status", "PROCESSING"))
               .andExpect(status().isNotFound());
    }
    
    @Test
    void getTasksByStatus_NullStatus_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/tasks/status"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string(containsString("Validation Failed")));
    }

    // ------------------------------ PUT /tasks/{id} ------------------------------
    @Test
    void updateTask_ValidData_ReturnsUpdatedTask() throws Exception {
        Task updatedTask = validTask;
        updatedTask.setTitle("Updated Task");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.valueOf("COMPLETED"));
        Mockito.when(taskService.updateTask(eq(1L), Mockito.any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(put("/tasks/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(updatedTask)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.title", is("Updated Task")))
               .andExpect(jsonPath("$.status", is("COMPLETED")));
    }

    @Test
    void updateTask_InvalidData_ReturnsBadRequest() throws Exception {
        String invalidTask = """
        		{
        		"title": "",
        		"description": "",
        		"status": "NOTCOMPLETED"
        		}
        		""";
        
        mockMvc.perform(put("/tasks/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(invalidTask))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").value(containsString("Validation Failed")));
    }

    @Test
    void updateTask_NonExistentId_ReturnsNotFound() throws Exception {
        Task updatedTask = new Task("Updated Task", "Updated Description", "COMPLETED");
        Mockito.when(taskService.updateTask(999L, updatedTask)).thenThrow(new EntityNotFoundException("Task not found."));

        mockMvc.perform(put("/tasks/999")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(updatedTask)))
               .andExpect(status().isNotFound());
    }

    // ------------------------------ POST /tasks ------------------------------
    @Test
    void createTask_ValidData_ReturnsCreated() throws Exception {
        // Create a new task that will be returned by the service
        Task savedTask = new Task("Valid Task", "Valid Description", "PENDING");
        savedTask.setId(1L); // Simulate database save by setting an ID

        // When saveTask is called with any Task object, return our savedTask
        Mockito.when(taskService.saveTask(Mockito.any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/tasks")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(validTask)))
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.description", is("Valid Description")))
               .andExpect(jsonPath("$.title", is("Valid Task")))
               .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    void createTask_InvalidData_ReturnsBadRequest() throws Exception {
        Task invalidTask = new Task("", "", "PENDING");

        mockMvc.perform(post("/tasks")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(invalidTask)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.error").value(containsString("Validation Failed")));
    }

    // ------------------------------ DELETE /tasks/{id} ------------------------------
    @Test
    void deleteTask_ValidId_ReturnsSuccess() throws Exception {
    	Mockito.when(taskService.existsById(1L)).thenReturn(true);
        Mockito.doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Task Successfully Deleted"));
    }

    @Test
    void deleteTask_NonExistentId_ReturnsNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Task not found.")).when(taskService).deleteTask(999L);
        Mockito.when(taskService.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/tasks/999"))
               .andExpect(status().isNotFound())
               .andExpect(content().string(containsString("Task Not Found")));
    }
}