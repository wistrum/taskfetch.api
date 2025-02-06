//DATA REPRESENTATION
package com.wistrum.taskfetch.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Task {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@NotEmpty
	@Size(min = 1, message = "Title is required.")
	private String title;
	
	@NotNull
	@NotEmpty
	@Size(min = 1, message = "Description is required.")
	private String description;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	private TaskStatus status;
	
	public Task(String title, String description, String status) {
		this.title = title;
		this.description = description;
		this.status = TaskStatus.valueOf(status.toUpperCase());
	}
	public Task() {};
	
	//Getters:
	public long getId() { return id; }
	public String getTitle() { return title; }
	public String getDescription() { return description; }
	public TaskStatus getStatus () { return status;}
	//Setters:
	public void setTitle(String title) { this.title = title; }
	public void setDescription(String description) { this.description = description; }
	public void setStatus(TaskStatus status) { this.status = status; }
	public void setId(long id) { this.id = id; }
}