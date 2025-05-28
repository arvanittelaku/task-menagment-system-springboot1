    package com.example.taskmenagmentsystemspringboot1.entities.task;

    import com.example.taskmenagmentsystemspringboot1.entities.user.User;
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

        @ManyToOne
        @JoinColumn(name = "assigned_to_id",nullable = false)
        private User assignedTo;

        @ManyToOne
        @JoinColumn(name = "created_by_id",nullable = false)
        private User createdBy;


    }
