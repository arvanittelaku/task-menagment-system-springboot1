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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @OneToMany(mappedBy = "createdBy")
    @Column(nullable = false)
    private List<Task> tasks;

    @OneToMany(mappedBy = "assignedTo")
    @Column(nullable = false)
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "assignedTo")
    @Column(nullable = false)
    private List<Task> completedTasks;

    @OneToMany(mappedBy = "assignedTo")
    @Column(nullable = false)
    private List<Task> canceledTasks;

    @OneToMany(mappedBy = "assignedTo")
    @Column(nullable = false)
    private List<Task> inProgressTasks;

    @OneToMany(mappedBy = "assignedTo")
    @Column(nullable = false)
    private List<Task> pendingTasks;



}
