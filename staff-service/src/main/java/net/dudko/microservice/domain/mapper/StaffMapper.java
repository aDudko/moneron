package net.dudko.microservice.domain.mapper;

import net.dudko.microservice.domain.entity.Staff;
import net.dudko.microservice.model.dto.StaffDto;

public class StaffMapper {

    public static Staff mapToStaff(StaffDto staffDto) {
        return Staff.builder()
                .id(staffDto.getId())
                .firstName(staffDto.getFirstName())
                .lastName(staffDto.getLastName())
                .email(staffDto.getEmail())
                .status(staffDto.getStatus())
                .departmentCode(staffDto.getDepartmentCode())
                .build();
    }

    public static StaffDto mapToStaffDto(Staff staff) {
        return StaffDto.builder()
                .id(staff.getId())
                .firstName(staff.getFirstName())
                .lastName(staff.getLastName())
                .email(staff.getEmail())
                .status(staff.getStatus())
                .departmentCode(staff.getDepartmentCode())
                .build();
    }

}
