package com.example.taskmenagmentsystemspringboot1.repositories;

import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findAllByAssignedTo(User assignedTo);

    List<Task> findAllByAssignedToId(Long id);
}
