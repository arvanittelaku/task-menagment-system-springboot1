package com.example.taskmenagmentsystemspringboot1.dtos.task;

import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatus {
    private TaskStatus status;
}
