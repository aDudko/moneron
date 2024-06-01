package net.dudko.microservice.domain.mapper;

import net.dudko.microservice.domain.entity.Employee;
import net.dudko.microservice.model.dto.EmployeeDto;

public class EmployeeMapper {

    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        return Employee.builder()
                .id(employeeDto.getId())
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .status(employeeDto.getStatus())
                .departmentCode(employeeDto.getDepartmentCode())
                .officeCode(employeeDto.getOfficeCode())
                .build();
    }

    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .status(employee.getStatus())
                .departmentCode(employee.getDepartmentCode())
                .officeCode(employee.getOfficeCode())
                .build();
    }

}
