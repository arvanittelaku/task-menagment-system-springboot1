package com.example.taskmenagmentsystemspringboot1.dtos.task;

import com.example.taskmenagmentsystemspringboot1.dtos.user.UserDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskPriority;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewTaskDto {

    @NotNull(message = "Id is required")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 5, message = "Title must be at least 5 characters long")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @NotNull(message = "Status is required")
    private TaskStatus status;

    @NotNull(message = "Priority is required")
    private TaskPriority priority;

    @NotNull(message = "Deadline is required")
    private LocalDate deadline;

    @NotNull(message = "Created at is required")
    private LocalDate createdAt;

    @NotNull(message = "Assigned to is required")
    private UserDto assignedTo;

    @NotNull(message = "Created by is required")
    private UserDto createdBy;

    private boolean canEdit;


}
