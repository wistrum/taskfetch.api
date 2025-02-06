package com.wistrum.taskfetch.api.controller;

import com.wistrum.taskfetch.api.exception.IllegalInputException;
import com.wistrum.taskfetch.api.model.*;
import com.wistrum.taskfetch.api.service.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
		catch (IllegalInputException e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
		catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/status")
	public ResponseEntity<?> getTasksByStatus(@Valid @RequestParam String status){
		try {
			TaskStatus getStatus = TaskStatus.valueOf(status);
			List<Task> tasks = taskService.getTasksByStatus(getStatus);
			if(tasks.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(tasks);
		}
		catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Status: " + status);
		}
	}
	@PutMapping("/{id}")
	public ResponseEntity<?> updateTask(@PathVariable long id, @Valid @RequestBody Task task) {
		try{
			Task updatedTask = taskService.updateTask(id,  task);
			if(updatedTask == null) {
				return ResponseEntity.notFound().build();
			}
			return ResponseEntity.ok(updatedTask);
		}
		catch (IllegalInputException e){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation Failed" + e.getMessage());
		}
	}
	@PostMapping
	public ResponseEntity<?> saveTask(@Valid @RequestBody Task task) {
		try{
			Task newTask = taskService.saveTask(task);
			return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
		}
		catch (DataIntegrityViolationException e) {
			return ResponseEntity.unprocessableEntity().build();
		}
		catch (IllegalInputException e) {
			return ResponseEntity.badRequest().body("Error: " + e.getMessage());
		}
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteTask(@PathVariable long id) {
		try{
			taskService.deleteTask(id);
			return ResponseEntity.ok("Task Successfully Deleted");
		}
		catch (IllegalInputException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}