package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {
    void deleteTask(Long taskId, Long userId);
    ViewTaskDto updateTask(Long taskId, UpdateTaskDto updateTaskDto, Long userId);
    ViewTaskDto getTask(Long taskId,Long userId);
    ViewTaskDto createTask(CreateTaskDto task,Long userId);
    List<ViewTaskDto> getTasksForCurrentUser(Long id);
    Task assignTaskToUser(User user,Task task);
    void updateTaskStatus(Long taskId, Long userId, TaskStatus newStatus);
    List<ViewTaskDto> findAll();

}
