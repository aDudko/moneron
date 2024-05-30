package net.dudko.microservice;


import net.dudko.microservice.domain.entity.Staff;
import net.dudko.microservice.model.dto.StaffDto;
import net.dudko.microservice.model.dto.StaffStatus;

public class TestUtil {

    public static Staff getValidEntity() {
        return Staff.builder()
                .id(1L)
                .firstName("Test first name")
                .lastName("Test last name")
                .email("test@mail.com")
                .status(StaffStatus.CREATED)
                .departmentCode("IT")
                .officeCode("001")
                .build();
    }

    public static StaffDto getValidDto() {
        return StaffDto.builder()
                .id(1L)
                .firstName("Test first name")
                .lastName("Test last name")
                .email("test@mail.com")
                .status(StaffStatus.CREATED)
                .departmentCode("IT")
                .officeCode("001")
                .build();
    }

}
