package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.EmployeeDto;

import java.util.List;

public interface StaffService {

    EmployeeDto create(EmployeeDto staffDto);

    ApiResponseDto getById(Long id);

    EmployeeDto getByEmail(String email);

    List<EmployeeDto> getStaff();

    EmployeeDto update(Long id, EmployeeDto staffDto);

    void delete(Long id);

}
