package com.example.taskmenagmentsystemspringboot1.repositories;

import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskPriority;
import com.example.taskmenagmentsystemspringboot1.entities.task.TaskStatus;
import com.example.taskmenagmentsystemspringboot1.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByAssignedTo(User assignedTo);

    List<Task> findAllByAssignedToId(Long id);

    List<Task> findAllByCreatedBy_Id(Long id);

    List<Task> findAllByAssignedTo_Id(Long id);

    // Paginated queries
    Page<Task> findAllByCreatedBy_Id(Long creatorId, Pageable pageable);

    Page<Task> findAllByAssignedTo_Id(Long assigneeId, Pageable pageable);

    // Find all tasks where user is either creator OR assignee
    @Query("SELECT t FROM Task t WHERE t.createdBy.id = :userId OR t.assignedTo.id = :userId")
    Page<Task> findAllByUserIdAsCreatorOrAssignee(@Param("userId") Long userId, Pageable pageable);

    // Statistics queries - count by various criteria for a specific user
    @Query("SELECT COUNT(t) FROM Task t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId) AND t.status = :status")
    Long countByUserIdAndStatus(@Param("userId") Long userId, @Param("status") TaskStatus status);

    @Query("SELECT COUNT(t) FROM Task t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId) AND t.priority = :priority")
    Long countByUserIdAndPriority(@Param("userId") Long userId, @Param("priority") TaskPriority priority);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.createdBy.id = :userId")
    Long countByCreatedBy(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo.id = :userId")
    Long countByAssignedTo(@Param("userId") Long userId);

    @Query("SELECT COUNT(t) FROM Task t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId) AND t.deadline < :today AND t.status NOT IN ('COMPLETED', 'CANCELED')")
    Long countOverdueTasks(@Param("userId") Long userId, @Param("today") LocalDate today);

    @Query("SELECT COUNT(t) FROM Task t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId) AND t.deadline = :today")
    Long countTasksDueToday(@Param("userId") Long userId, @Param("today") LocalDate today);

    // Filtering with dynamic criteria
    @Query("SELECT t FROM Task t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId) " +
            "AND (:status IS NULL OR t.status = :status) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:fromDate IS NULL OR t.deadline >= :fromDate) " +
            "AND (:toDate IS NULL OR t.deadline <= :toDate)")
    Page<Task> findAllWithFilters(@Param("userId") Long userId,
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable);

    // Search tasks by title or description
    @Query("SELECT t FROM Task t WHERE (t.createdBy.id = :userId OR t.assignedTo.id = :userId) " +
            "AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<Task> searchTasks(@Param("userId") Long userId,
            @Param("searchTerm") String searchTerm,
            Pageable pageable);
}
