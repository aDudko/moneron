package net.dudko.microservice.domain.mapper;

import net.dudko.microservice.domain.entity.Task;
import net.dudko.microservice.model.dto.TaskDto;

public class TaskMapper {

    public static Task mapToTask(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .departmentCode(taskDto.getDepartmentCode())
                .officeCode(taskDto.getOfficeCode())
                .employeeEmail(taskDto.getEmployeeEmail())
                .build();
    }

    public static TaskDto mapToTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .departmentCode(task.getDepartmentCode())
                .officeCode(task.getOfficeCode())
                .employeeEmail(task.getEmployeeEmail())
                .build();
    }

}
