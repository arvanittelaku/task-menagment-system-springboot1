package com.example.taskmenagmentsystemspringboot1.entities.user;

import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private UserRole role;
    @Column(nullable = false)
    private List<Task> tasks;
    @Column(nullable = false)
    private List<Task> assignedTasks;
    @Column(nullable = false)
    private List<Task> completedTasks;
    @Column(nullable = false)
    private List<Task> cancelledTasks;
    @Column(nullable = false)
    private List<Task> inProgressTasks;
    @Column(nullable = false)
    private List<Task> pendingTasks;



}
