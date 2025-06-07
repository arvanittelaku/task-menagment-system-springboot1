package com.example.taskmenagmentsystemspringboot1.mappers;

import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mappings({
            @Mapping(target = "id", source = "task.id"),
            @Mapping(target = "title", source = "task.title"),
            @Mapping(target = "description", source = "task.description"),
            @Mapping(target = "status", source = "task.status"),
            @Mapping(target = "priority", source = "task.priority"),
            @Mapping(target = "deadline", source = "task.deadline"),
            @Mapping(target = "createdAt", source = "task.createdAt"),
            @Mapping(target = "createdBy", source = "task.createdBy"),
            @Mapping(target = "assignedTo", source = "task.assignedTo")
    })
    ViewTaskDto fromEntityToView(Task task, Long currentUserId);

    // Optionally, you might still want a simpler version:
    ViewTaskDto fromEntityToView(Task task);
}
