//DB OPERATIONS
package com.wistrum.taskfetch.api.repository;

import com.wistrum.taskfetch.api.model.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long>{
	List<Task> findByStatus(TaskStatus status);
}