package com.example.taskmenagmentsystemspringboot1.service;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.TaskStatisticsDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskPriority;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface TaskService {
    void deleteTask(Long taskId, Long userId);

    ViewTaskDto updateTask(Long taskId, UpdateTaskDto updateTaskDto, Long userId);

    ViewTaskDto getTask(Long taskId, Long userId);

    ViewTaskDto createTask(CreateTaskDto task, Long userId);

    List<ViewTaskDto> getTasksForCurrentUser(Long id);

    void updateTaskStatus(Long taskId, Long userId, TaskStatus newStatus);

    TaskStatisticsDto getStatistics(Long userId);

    // Paginated methods
    Page<ViewTaskDto> findAll(Pageable pageable);

    Page<ViewTaskDto> getTasksCreatedBy(Long creatorId, Pageable pageable);

    Page<ViewTaskDto> getTasksAssignedTo(Long assigneeId, Pageable pageable);

    Page<ViewTaskDto> getUserTasks(Long userId, Pageable pageable); // Tasks created by OR assigned to user

    Page<ViewTaskDto> filterTasks(Long userId, TaskStatus status, TaskPriority priority,
            LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Page<ViewTaskDto> searchTasks(Long userId, String searchTerm, Pageable pageable);

    // Legacy non-paginated methods (kept for backward compatibility)
    List<ViewTaskDto> findAll();

    List<ViewTaskDto> getTasksCreatedBy(Long creatorId);

    List<ViewTaskDto> getTasksAssignedTo(Long assigneeId);

}
