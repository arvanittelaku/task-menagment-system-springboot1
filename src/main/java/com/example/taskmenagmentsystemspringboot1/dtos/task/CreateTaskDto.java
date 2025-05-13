package com.example.taskmenagmentsystemspringboot1.dtos.task;

import com.example.taskmenagmentsystemspringboot1.entities.task.TaskPriority;
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
public class CreateTaskDto {

    @NotBlank(message = "Title is required")
    @NotNull(message = "Title is required")
    @Size(min = 5, message = "Title must be at least 2 characters long")
    private String title;

    @NotBlank(message = "Description is required")
    @NotNull(message = "Description is required")
    @Size(min = 10, message = "Description must be at least 2 characters long")
    private String description;

    private LocalDate deadline;
    private TaskPriority priority;
    private Long assignedToId;
    private User createdBy;
}
