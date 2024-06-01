package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Department;
import net.dudko.microservice.model.dto.DepartmentDto;

public class TestUtil {

    public static final String MS_NAME = "DEPARTMENT-MICROSERVICE: ";

    public static Department getValidEntity() {
        return Department.builder()
                .id(1L)
                .name("Test Department Name")
                .description("Test Department Description")
                .code("Test Department Code")
                .build();
    }

    public static DepartmentDto getValidDto() {
        return DepartmentDto.builder()
                .id(1L)
                .name("Test Department Name")
                .description("Test Department Description")
                .code("Test Department Code")
                .build();
    }
}
