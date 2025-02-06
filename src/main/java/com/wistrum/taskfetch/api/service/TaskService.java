package com.wistrum.taskfetch.api.service;

import com.wistrum.taskfetch.api.exception.*;
import com.wistrum.taskfetch.api.model.*;
import com.wistrum.taskfetch.api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
	private final TaskRepository taskRepository;
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}
	public List<Task> getAllTasks(){
		return taskRepository.findAll();
	}
	public Task getTaskById(long id) throws IllegalInputException {
		Task task = taskRepository.findById(id).orElse(null);
		if (task == null) {
			throw new EntityNotFoundException("Task not found.");
		}
		return task;
	}
	public List<Task> getTasksByStatus(TaskStatus status) throws IllegalArgumentException {
		return taskRepository.findByStatus(status);
	}
	public Task updateTask(long id, Task task) throws IllegalInputException {
		Task oldTask = taskRepository.findById(id).orElse(null);
		if(oldTask == null) {
			throw new EntityNotFoundException("Task not found.");
		}
		if(task.getStatus() == null || task.getDescription() == null || task.getTitle() == null
				|| task.getStatus().toString().isEmpty() || task.getDescription().isEmpty() 
				|| task.getTitle().isEmpty() || task.getId() < 0) {
			throw new IllegalInputException ("Invalid Input");
		}
		oldTask.setDescription(task.getDescription());
		oldTask.setStatus(task.getStatus());
		oldTask.setTitle(task.getTitle());
		return taskRepository.save(oldTask);
	}
	public Task saveTask(Task task) throws IllegalInputException {
		Task newTask = taskRepository.save(task);
		return newTask;
	}
	public void deleteTask(long id) throws IllegalInputException {
		taskRepository.deleteById(id);
	}
}