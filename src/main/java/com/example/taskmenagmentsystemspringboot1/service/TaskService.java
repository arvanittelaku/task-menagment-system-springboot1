package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {
    Task createTask(CreateTaskDto createTaskDto);
    void deleteTask(Long id);
    Task updateTask(Long id, UpdateTaskDto updateTaskDto);
    ViewTaskDto getTask(Long id);
}
