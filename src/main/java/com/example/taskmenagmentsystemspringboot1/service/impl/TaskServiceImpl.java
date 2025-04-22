package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.mappers.TaskMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.TaskRepository;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    @Override
    public Task createTask(CreateTaskDto createTaskDto) {
        return taskMapper.fromCreateToEntity(createTaskDto);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.removeById(id);
    }

    @Override
    public Task updateTask(Long id, UpdateTaskDto updateTaskDto) {
        var existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            var task = taskMapper.fromUpdateToEntity(updateTaskDto);
            task.setId(id);
            return taskRepository.save(task);
        }
        return null;
    }

    @Override
    public ViewTaskDto getTask(Long id) {
        var existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            return taskMapper.fromEntityToView(existingTask.get());
        }
        return null;
    }
}
