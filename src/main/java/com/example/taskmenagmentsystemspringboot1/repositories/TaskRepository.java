package com.example.taskmenagmentsystemspringboot1.repositories;

import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {

}
