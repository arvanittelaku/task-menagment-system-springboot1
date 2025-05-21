package com.example.taskmenagmentsystemspringboot1.entities.user;

import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
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
    private List<Task> tasks;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> assignedTasks;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> completedTasks;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> canceledTasks;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> inProgressTasks;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> pendingTasks;



    public static User createSuperAdmin() {
        return User.builder()
                .username("superadmin")
                .password("superadmin")
                .email("superadmin@gmail.com")
                .role(UserRole.ADMIN)
                .build();
    }
}
