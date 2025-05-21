package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    Task createTask(CreateTaskDto createTaskDto);
    void deleteTask(Long id);
    Task updateTask(Long id, UpdateTaskDto updateTaskDto);
    ViewTaskDto getTask(Long id);
    Task createTask(Task task);
    List<Task> getTasksForCurrentUser(User user);
    Task assignTaskToUser(User user,Task task);
    Task updateTaskStatus(Long taskId, TaskStatus newStatus);

}
