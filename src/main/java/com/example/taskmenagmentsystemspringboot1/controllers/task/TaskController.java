package com.example.taskmenagmentsystemspringboot1.controllers.task;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.TaskStatisticsDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskStatus;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskPriority;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;

import java.time.LocalDate;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<ViewTaskDto>> findAll(
            @AuthenticationPrincipal AppUserDetails principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        String role = principal.getRole();
        Long userId = principal.getId();

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ViewTaskDto> tasks;

        switch (role) {
            case "ADMIN":
                tasks = taskService.findAll(pageable);
                break;
            case "MANAGER":
                tasks = taskService.getTasksCreatedBy(userId, pageable);
                break;
            case "USER":
                // Users see both tasks they created AND tasks assigned to them
                tasks = taskService.getUserTasks(userId, pageable);
                break;
            default:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewTaskDto> findById(@PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails principal) {
        return ResponseEntity.ok(taskService.getTask(id, principal.getId()));
    }

    @PostMapping("/create")
    public ResponseEntity<ViewTaskDto> createTask(
            @Valid @RequestBody CreateTaskDto dto,
            @AuthenticationPrincipal AppUserDetails principal) {

        ViewTaskDto task = taskService.createTask(dto, principal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateTaskDto updateTaskDto,
            @AuthenticationPrincipal AppUserDetails principal) {

        var currentUser = userService.findUserByUsername(principal.getUsername());
        var task = taskService.getTask(id, principal.getId()); // ✅ updated method call

        if (!task.getCreatedBy().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not allowed to update this task.");
        }

        return ResponseEntity.ok(taskService.updateTask(id, updateTaskDto, principal.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id,
            @AuthenticationPrincipal AppUserDetails principal) {
        taskService.deleteTask(id, principal.getId());
        return ResponseEntity.ok("Task deleted successfully");
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatus request,
            @AuthenticationPrincipal AppUserDetails principal) {
        try {
            taskService.updateTaskStatus(id, principal.getId(), request.getStatus());
            return ResponseEntity.ok(Map.of("message", "Task status updated successfully"));
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/dashboard")
    public ResponseEntity<TaskStatisticsDto> getStatistics(
            @AuthenticationPrincipal AppUserDetails principal) {
        TaskStatisticsDto statistics = taskService.getStatistics(principal.getId());
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ViewTaskDto>> filterTasks(
            @AuthenticationPrincipal AppUserDetails principal,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ViewTaskDto> filteredTasks = taskService.filterTasks(
                principal.getId(), status, priority, fromDate, toDate, pageable);

        return ResponseEntity.ok(filteredTasks);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ViewTaskDto>> searchTasks(
            @AuthenticationPrincipal AppUserDetails principal,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ViewTaskDto> searchResults = taskService.searchTasks(
                principal.getId(), query, pageable);

        return ResponseEntity.ok(searchResults);
    }
}
