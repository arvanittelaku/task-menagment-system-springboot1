package com.example.taskmenagmentsystemspringboot1.entities.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private TaskStatus status;
    @Column(nullable = false)
    private TaskPriority priority;
    @Column(nullable = false)
    private LocalDate deadline;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column(nullable = false)
    private User assignedTo;
    @Column(nullable = false)
    private User createdBy;


}
