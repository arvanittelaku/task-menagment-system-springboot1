// src/main/java/com/example/taskmenagmentsystemspringboot1/entities/task/Task.java
package com.example.taskmenagmentsystemspringboot1.entities.task;

import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*; // <-- Ensure these are imported (Getter, Setter, AllArgsConstructor, NoArgsConstructor, Entity are already there)

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"assignedTo", "createdBy"}) // <-- ADD THIS LINE
@EqualsAndHashCode(exclude = {"assignedTo", "createdBy"}) // <-- ADD THIS LINE
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Column(nullable = false)
    private LocalDate deadline;

    @Column(nullable = false)
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY) // <-- Ensure fetch type is LAZY (usually default for ManyToOne, but good to be explicit)
    @JoinColumn(name = "assigned_to_id", nullable = false)
    @JsonIgnore
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY) // <-- Ensure fetch type is LAZY
    @JoinColumn(name = "created_by_id", nullable = false)
    @JsonIgnore
    private User createdBy;
}