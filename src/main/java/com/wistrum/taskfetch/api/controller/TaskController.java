package com.wistrum.taskfetch.api.controller;

import com.wistrum.taskfetch.api.model.*;
import com.wistrum.taskfetch.api.service.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/tasks")
public class TaskController {
	private TaskService taskService;
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	@GetMapping
	public ResponseEntity<List<Task>> getAllTasks(){
		List<Task> tasks = taskService.getAllTasks();
		if(tasks.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(tasks);
	}
	@GetMapping ("/{id}")
	public ResponseEntity<?> getTaskById(@PathVariable long id) {
		try {
			Task task = taskService.getTaskById(id);
			return ResponseEntity.ok(task);
		}
		catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/status")
	public ResponseEntity<?> getTasksByStatus(@Valid @RequestParam(required = false) String status){
		if(status == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Failed");
		}
		try {
			TaskStatus getStatus = TaskStatus.valueOf(status.toUpperCase());
			List<Task> tasks = taskService.getTasksByStatus(getStatus);
			if(tasks.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(tasks);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Status");
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateTask(@PathVariable long id, @Valid @RequestBody Task task) {
			Task updatedTask = taskService.updateTask(id,  task);
			if(updatedTask == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(updatedTask);
	}
	@PostMapping
	public ResponseEntity<?> saveTask(@Valid @RequestBody Task task) {
		Task newTask = taskService.saveTask(task);
		return ResponseEntity.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(newTask);
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTask(@PathVariable long id) {
		if(!taskService.existsById(id)) {
			throw new EntityNotFoundException("Task Not Found");
		}
		taskService.deleteTask(id);
		return ResponseEntity.ok("Task Successfully Deleted");
	}
}