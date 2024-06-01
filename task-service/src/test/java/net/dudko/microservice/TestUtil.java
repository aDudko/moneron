package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Task;
import net.dudko.microservice.domain.mapper.TaskMapper;
import net.dudko.microservice.model.dto.TaskDto;
import net.dudko.microservice.model.dto.TaskStatus;

public class TestUtil {

    public static final String MS_NAME = "TASK-MICROSERVICE: ";

    public static Task getValidEntity() {
        return Task.builder()
                .id(1L)
                .title("Test Task Title")
                .description("Test Task Description")
                .status(TaskStatus.TODO)
                .departmentCode("IT")
                .officeCode("001")
                .employeeEmail("test@mail.com")
                .build();
    }

    public static TaskDto getValidDto() {
        return TaskMapper.mapToTaskDto(getValidEntity());
    }

}
