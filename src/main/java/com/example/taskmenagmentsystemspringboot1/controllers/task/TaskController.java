package com.example.taskmenagmentsystemspringboot1.controllers.task;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskStatus;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.example.taskmenagmentsystemspringboot1.security.AppUserDetails;
import com.example.taskmenagmentsystemspringboot1.service.TaskService;
import com.example.taskmenagmentsystemspringboot1.service.UserService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5174")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<ViewTaskDto>> findAll() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ViewTaskDto> findById(@PathVariable Long id,
                                                @AuthenticationPrincipal AppUserDetails principal) {
        return ResponseEntity.ok(taskService.getTask(id, principal.getId()));
    }

    @PostMapping("/create")
    public ResponseEntity<ViewTaskDto> createTask(@RequestBody CreateTaskDto dto,
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
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal AppUserDetails principal) {
        taskService.deleteTask(id, principal.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateTaskStatus request,
            @AuthenticationPrincipal AppUserDetails principal
    ) {
        try {
            taskService.updateTaskStatus(id, principal.getId(), request.getStatus());
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<ViewTaskDto>> getTasksForCurrentUser(@AuthenticationPrincipal AppUserDetails principal) {
        List<ViewTaskDto> tasks = taskService.getTasksForCurrentUser(principal.getId());
        return ResponseEntity.ok(tasks);
    }


}
