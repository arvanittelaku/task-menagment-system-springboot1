package com.example.taskmenagmentsystemspringboot1.dtos.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatisticsDto {

    private Long totalTasks;
    private Long tasksCreated; // Tasks created by user
    private Long tasksAssigned; // Tasks assigned to user

    // Count by status
    private Long pendingTasks;
    private Long inProgressTasks;
    private Long completedTasks;
    private Long canceledTasks;

    // Count by priority
    private Long highPriorityTasks;
    private Long mediumPriorityTasks;
    private Long lowPriorityTasks;

    // Completion rate (percentage)
    private Double completionRate;

    // Additional statistics
    private Long overdueTasks; // Tasks past deadline
    private Long todayTasks; // Tasks due today
}
