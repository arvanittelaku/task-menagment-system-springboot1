package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.mappers.TaskMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.TaskRepository;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private TaskMapper taskMapper;
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

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksForCurrentUser(User user) {
        return userRepository.findById(user.getId()).get().getTasks();
    }

    @Override
    public Task assignTaskToUser(User user,Task task) {
        var task1 = taskRepository.findById(task.getId()).get();
        var user1 = userRepository.findById(user.getId()).get();
        task.setAssignedTo(user);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getTasksByUserId(Long id) {
        return taskRepository.findAllByAssignedTo_Id(id);
    }
}
