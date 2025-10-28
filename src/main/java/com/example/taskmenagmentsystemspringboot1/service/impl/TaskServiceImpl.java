package com.example.taskmenagmentsystemspringboot1.service.impl;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.TaskStatisticsDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskPriority;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.exceptions.ResourceNotFoundException;
import com.example.taskmenagmentsystemspringboot1.mappers.TaskMapper;
import com.example.taskmenagmentsystemspringboot1.repositories.TaskRepository;
import com.example.taskmenagmentsystemspringboot1.repositories.UserRepository;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
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

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isCreator = task.getCreatedBy().getId().equals(userId);

        if (!isAdmin && !isCreator) {
            log.warn("Access denied: User {} attempted to delete task {} without permission",
                    userId, taskId);
            throw new AccessDeniedException("You are not allowed to delete this task.");
        }

        log.info("Task {} deleted by user {}", taskId, userId);
        taskRepository.delete(task);
    }

    @Override
    public ViewTaskDto updateTask(Long taskId, UpdateTaskDto updateTaskDto, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        if (!task.getCreatedBy().getId().equals(userId)) {
            log.warn("Access denied: User {} attempted to update task {} without permission",
                    userId, taskId);
            throw new AccessDeniedException("You are not allowed to update this task");
        }

        log.info("Updating task {} by user {}", taskId, userId);

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
            log.warn("Access denied: User {} attempted to update status of task {} without permission",
                    userId, taskId);
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
            log.warn("Invalid status transition from {} to {} for task {}",
                    currentStatus, newStatus, taskId);
            throw new IllegalStateException("Invalid status change from " + currentStatus + " to " + newStatus);
        }

        log.info("Task {} status changed from {} to {} by user {}",
                taskId, currentStatus, newStatus, userId);
        task.setStatus(newStatus);
        taskRepository.save(task);
    }

    // Paginated methods
    @Override
    public Page<ViewTaskDto> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(taskMapper::fromEntityToView);
    }

    @Override
    public Page<ViewTaskDto> getTasksCreatedBy(Long creatorId, Pageable pageable) {
        return taskRepository.findAllByCreatedBy_Id(creatorId, pageable)
                .map(taskMapper::fromEntityToView);
    }

    @Override
    public Page<ViewTaskDto> getTasksAssignedTo(Long assigneeId, Pageable pageable) {
        return taskRepository.findAllByAssignedTo_Id(assigneeId, pageable)
                .map(taskMapper::fromEntityToView);
    }

    @Override
    public Page<ViewTaskDto> getUserTasks(Long userId, Pageable pageable) {
        return taskRepository.findAllByUserIdAsCreatorOrAssignee(userId, pageable)
                .map(taskMapper::fromEntityToView);
    }

    @Override
    public Page<ViewTaskDto> filterTasks(Long userId, TaskStatus status, TaskPriority priority,
            LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        log.info("Filtering tasks for user {}: status={}, priority={}, from={}, to={}",
                userId, status, priority, fromDate, toDate);

        return taskRepository.findAllWithFilters(userId, status, priority, fromDate, toDate, pageable)
                .map(taskMapper::fromEntityToView);
    }

    @Override
    public Page<ViewTaskDto> searchTasks(Long userId, String searchTerm, Pageable pageable) {
        log.info("Searching tasks for user {} with term: {}", userId, searchTerm);

        return taskRepository.searchTasks(userId, searchTerm, pageable)
                .map(taskMapper::fromEntityToView);
    }

    // Legacy non-paginated methods (kept for backward compatibility)
    @Override
    public List<ViewTaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::fromEntityToView)
                .toList();
    }

    @Override
    public List<ViewTaskDto> getTasksCreatedBy(Long creatorId) {
        return taskRepository.findAllByCreatedBy_Id(creatorId).stream()
                .map(taskMapper::fromEntityToView)
                .toList();
    }

    @Override
    public List<ViewTaskDto> getTasksAssignedTo(Long id) {
        return taskRepository.findAllByAssignedTo_Id(id).stream()
                .map(taskMapper::fromEntityToView)
                .toList();
    }

    @Override
    public ViewTaskDto getTask(Long taskId, Long currentUserId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + currentUserId));

        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isCreator = task.getCreatedBy().getId().equals(currentUserId);
        boolean isAssigned = task.getAssignedTo().getId().equals(currentUserId);

        if (!(isAdmin || isCreator || isAssigned)) {
            throw new AccessDeniedException("You do not have permission to view this task.");
        }

        return taskMapper.fromEntityToView(task, currentUserId);
    }

    public ViewTaskDto createTask(CreateTaskDto dto, Long creatorUserId) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Creator user not found"));

        // If assignedToId is not provided, assign task to creator (personal task)
        User assignedTo;
        if (dto.getAssignedToId() != null) {
            assignedTo = userRepository.findById(dto.getAssignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
        } else {
            assignedTo = creator; // Personal task
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(TaskStatus.PENDING);
        task.setDeadline(dto.getDeadline());
        task.setCreatedAt(LocalDate.now());
        task.setCreatedBy(creator);
        task.setAssignedTo(assignedTo);

        Task savedTask = taskRepository.save(task);

        if (dto.getAssignedToId() != null && !dto.getAssignedToId().equals(creatorUserId)) {
            log.info("Task {} created by user {} and assigned to user {}",
                    savedTask.getId(), creatorUserId, dto.getAssignedToId());
        } else {
            log.info("Personal task {} created by user {}",
                    savedTask.getId(), creatorUserId);
        }

        return taskMapper.fromEntityToView(savedTask);
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
    public TaskStatisticsDto getStatistics(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        LocalDate today = LocalDate.now();

        // Count by status
        Long pendingTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.PENDING);
        Long inProgressTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.IN_PROGRESS);
        Long completedTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.COMPLETED);
        Long canceledTasks = taskRepository.countByUserIdAndStatus(userId, TaskStatus.CANCELED);

        // Count by priority
        Long highPriorityTasks = taskRepository.countByUserIdAndPriority(userId, TaskPriority.HIGH);
        Long mediumPriorityTasks = taskRepository.countByUserIdAndPriority(userId, TaskPriority.MEDIUM);
        Long lowPriorityTasks = taskRepository.countByUserIdAndPriority(userId, TaskPriority.LOW);

        // Count created vs assigned
        Long tasksCreated = taskRepository.countByCreatedBy(userId);
        Long tasksAssigned = taskRepository.countByAssignedTo(userId);

        // Total tasks (created by OR assigned to user)
        Long totalTasks = pendingTasks + inProgressTasks + completedTasks + canceledTasks;

        // Completion rate calculation
        Double completionRate = totalTasks > 0 ? (completedTasks.doubleValue() / totalTasks.doubleValue()) * 100 : 0.0;

        // Additional statistics
        Long overdueTasks = taskRepository.countOverdueTasks(userId, today);
        Long todayTasks = taskRepository.countTasksDueToday(userId, today);

        log.info("Generated statistics for user {}: total={}, completed={}, rate={}%",
                userId, totalTasks, completedTasks, String.format("%.2f", completionRate));

        return TaskStatisticsDto.builder()
                .totalTasks(totalTasks)
                .tasksCreated(tasksCreated)
                .tasksAssigned(tasksAssigned)
                .pendingTasks(pendingTasks)
                .inProgressTasks(inProgressTasks)
                .completedTasks(completedTasks)
                .canceledTasks(canceledTasks)
                .highPriorityTasks(highPriorityTasks)
                .mediumPriorityTasks(mediumPriorityTasks)
                .lowPriorityTasks(lowPriorityTasks)
                .completionRate(completionRate)
                .overdueTasks(overdueTasks)
                .todayTasks(todayTasks)
                .build();
    }

}
