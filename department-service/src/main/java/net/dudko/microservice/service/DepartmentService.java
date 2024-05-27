package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.DepartmentDto;

import java.util.List;

public interface DepartmentService {

    DepartmentDto create(DepartmentDto departmentDto);

    DepartmentDto getById(Long id);

    List<DepartmentDto> getAll();

    DepartmentDto update(Long id, DepartmentDto departmentDto);

}
