package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Employee;
import net.dudko.microservice.model.dto.EmployeeDto;
import net.dudko.microservice.model.dto.EmployeeStatus;

public class TestUtil {

    public static final String MS_NAME = "STAFF-MICROSERVICE: ";

    public static Employee getValidEntity() {
        return Employee.builder()
                .id(1L)
                .firstName("Test FirstName")
                .lastName("Test LastName")
                .email("test@mail.com")
                .status(EmployeeStatus.CREATED)
                .departmentCode("IT")
                .officeCode("EMS")
                .build();
    }

    public static EmployeeDto getValidDto() {
        return EmployeeDto.builder()
                .id(1L)
                .firstName("Test FirstName")
                .lastName("Test LastName")
                .email("test@mail.com")
                .status(EmployeeStatus.CREATED)
                .departmentCode("IT")
                .officeCode("EMS")
                .build();
    }

}
