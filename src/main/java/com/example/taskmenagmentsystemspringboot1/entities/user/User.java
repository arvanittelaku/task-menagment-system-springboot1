package com.example.taskmenagmentsystemspringboot1.entities.user;

import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_manager_id", foreignKey = @ForeignKey(name = "fk_created_by_manager", value = ConstraintMode.CONSTRAINT))
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User createdByManager;


    @OneToMany(mappedBy = "createdBy")
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> assignedTasks;

    public static User createSuperAdmin(PasswordEncoder encoder) {
        return User.builder()
                .username("superadmin")
                .password(encoder.encode("superadmin"))
                .email("superadmin@gmail.com")
                .role(UserRole.ADMIN)
                .build();
    }
}
