package com.example.taskmenagmentsystemspringboot1.entities.user;

import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.fasterxml.jackson.annotation.JsonIgnore; // Ensure this import is present
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
@ToString(exclude = "tasks") // Good for Lombok's toString
@EqualsAndHashCode(exclude = "tasks") // Good for Lombok's equals/hashCode
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

    // ONE list for all tasks assigned to the user
    @OneToMany(mappedBy = "assignedTo")
    @JsonIgnore // <-- Correctly applied
    private List<Task> tasks;

    public static User createSuperAdmin(PasswordEncoder encoder) {
        return User.builder()
                .username("superadmin")
                .password(encoder.encode("superadmin"))
                .email("superadmin@gmail.com")
                .role(UserRole.ADMIN)
                .build();
    }
}