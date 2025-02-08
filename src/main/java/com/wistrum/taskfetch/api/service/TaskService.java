package com.wistrum.taskfetch.api.service;


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
	public Task getTaskById(long id) {
		Task task = taskRepository.findById(id).orElse(null);
		if (task == null) {
			throw new EntityNotFoundException("Task not found.");
		}
		return task;
	}
	public Boolean existsById(long id) {
		return taskRepository.existsById(id);
	}

	public List<Task> getTasksByStatus(TaskStatus status) throws IllegalArgumentException {
		return taskRepository.findByStatus(status);
	}
	public Task updateTask(long id, Task task) throws EntityNotFoundException {
		Task oldTask = taskRepository.findById(id).orElse(null);
		oldTask.setDescription(task.getDescription());
		oldTask.setStatus(task.getStatus());
		oldTask.setTitle(task.getTitle());
		return taskRepository.save(oldTask);
	}
	public Task saveTask(Task task) {
		Task newTask = taskRepository.save(task);
		return newTask;
	}
	public void deleteTask(long id) {
		taskRepository.deleteById(id);
	}
}