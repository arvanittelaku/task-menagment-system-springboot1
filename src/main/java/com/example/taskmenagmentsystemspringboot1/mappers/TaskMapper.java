package com.example.taskmenagmentsystemspringboot1.mappers;

import com.example.taskmenagmentsystemspringboot1.dtos.task.CreateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.UpdateTaskDto;
import com.example.taskmenagmentsystemspringboot1.dtos.task.ViewTaskDto;
import com.example.taskmenagmentsystemspringboot1.entities.task.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task fromCreateToEntity(CreateTaskDto createTaskDto);
    ViewTaskDto fromEntityToView(Task task);
    Task fromUpdateToEntity(UpdateTaskDto updateTaskDto);
}
