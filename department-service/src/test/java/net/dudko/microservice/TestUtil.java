package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Department;
import net.dudko.microservice.model.dto.DepartmentDto;

public class TestUtil {

    public static Department getValidDepartment() {
        return Department.builder()
                .id(1L)
                .name("Test Department Default. Name")
                .description("Test Department Default. Description")
                .code("TDD")
                .build();
    }

    public static DepartmentDto getValidDepartmentDto() {
        return DepartmentDto.builder()
                .id(1L)
                .name("Test Department Default. Name")
                .description("Test Department Default. Description")
                .code("TDD")
                .build();
    }
}
