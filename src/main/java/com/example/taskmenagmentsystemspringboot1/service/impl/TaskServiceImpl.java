package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.exceptions.ResourceNotFoundException;
import com.example.taskmenagmentsystemspringboot1.mappers.TaskMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.TaskRepository;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;



    @Override
    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to delete this task.");
        }

        taskRepository.delete(task);
    }

    @Override
    public ViewTaskDto updateTask(Long taskId, UpdateTaskDto updateTaskDto, Long userId) {
        // Find the task or throw exception if not found
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        if (!task.getCreatedBy().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to update this task");
        }


        if (updateTaskDto.getTitle() != null) {
            task.setTitle(updateTaskDto.getTitle());
        }
        
        if (updateTaskDto.getDescription() != null) {
            task.setDescription(updateTaskDto.getDescription());
        }
        
        if (updateTaskDto.getStatus() != null) {
            task.setStatus(updateTaskDto.getStatus());
        }
        
        if (updateTaskDto.getPriority() != null) {
            task.setPriority(updateTaskDto.getPriority());
        }
        
        if (updateTaskDto.getDeadline() != null) {
            task.setDeadline(updateTaskDto.getDeadline());
        }
        Task updatedTask = taskRepository.save(task);
        return taskMapper.fromEntityToView(updatedTask);
    }


    @Override
    public void updateTaskStatus(Long taskId, Long userId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!task.getAssignedTo().getId().equals(userId)) {
            throw new AccessDeniedException("You are not allowed to update this task.");
        }

        TaskStatus currentStatus = task.getStatus();
        boolean valid = switch (currentStatus) {
            case PENDING -> newStatus == TaskStatus.IN_PROGRESS ||
                    newStatus == TaskStatus.COMPLETED ||
                    newStatus == TaskStatus.CANCELED;

            case IN_PROGRESS -> newStatus == TaskStatus.COMPLETED ||
                    newStatus == TaskStatus.CANCELED;

            case COMPLETED, CANCELED -> false;
        };

        if (!valid) {
            throw new IllegalStateException("Invalid status change from " + currentStatus + " to " + newStatus);
        }

        task.setStatus(newStatus);
        taskRepository.save(task);
    }

    @Override
    public List<ViewTaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::fromEntityToView)
                .toList();
    }


    @Override
    public ViewTaskDto getTask(Long taskId, Long currentUserId) {
        System.out.println("Looking for task ID: " + taskId + ", for user: " + currentUserId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        System.out.println("Task found. AssignedTo: " + task.getAssignedTo().getId() + ", CreatedBy: " + task.getCreatedBy().getId());

        return taskMapper.fromEntityToView(task, currentUserId);
    }


    public ViewTaskDto createTask(CreateTaskDto dto, Long creatorUserId) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found"));

        User assignedTo = userRepository.findById(dto.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(TaskStatus.PENDING);
        task.setDeadline(dto.getDeadline());
        task.setCreatedAt(LocalDate.now());
        task.setCreatedBy(creator);
        task.setAssignedTo(assignedTo);

        taskRepository.save(task);
        return taskMapper.fromEntityToView(task);
    }


    @Override
    public List<ViewTaskDto> getTasksForCurrentUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return taskRepository.findAllByAssignedToId(userId).stream()
                .map(taskMapper::fromEntityToView)
                .toList();
    }




    @Override
    public Task assignTaskToUser(User user, Task task) {
        Task task1 = taskRepository.findById(task.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        User user1 = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (task1.getStatus() == TaskStatus.COMPLETED) {
            throw new RuntimeException("Cannot assign a completed task");
        }
        if (task1.getStatus() == TaskStatus.IN_PROGRESS) {
            throw new RuntimeException("Cannot assign an in-progress task to another user");
        }

        task1.setAssignedTo(user1);
        return taskRepository.save(task1);
    }












}
