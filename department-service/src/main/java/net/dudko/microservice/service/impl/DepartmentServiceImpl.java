package net.dudko.microservice.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.DepartmentMapper;
import net.dudko.microservice.domain.repository.DepartmentRepository;
import net.dudko.microservice.model.dto.DepartmentDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentDto create(DepartmentDto departmentDto) {
        if (departmentRepository.existsByCode(departmentDto.getCode())) {
            throw new ResourceDuplicatedException("Code of department already exists!");
        }
        var inDb = departmentRepository.save(DepartmentMapper.mapToDepartment(departmentDto));
        return DepartmentMapper.mapToDepartmentDto(inDb);
    }

    @Override
    public DepartmentDto getById(Long id) {
        var department = departmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Department with id: %d not found", id)));
        return DepartmentMapper.mapToDepartmentDto(department);
    }

    @Override
    public DepartmentDto getByCode(String code) {
        if (!departmentRepository.existsByCode(code)) {
            throw new ResourceNotFoundException(String.format("Department with code: %s not found", code));
        }
        var department = departmentRepository.findByCode(code);
        return DepartmentMapper.mapToDepartmentDto(department);
    }

    @Override
    public List<DepartmentDto> getAll() {
        var departments = departmentRepository.findAll();
        return departments.stream().map(DepartmentMapper::mapToDepartmentDto).toList();
    }

    @Override
    public DepartmentDto update(Long id, DepartmentDto departmentDto) {
        var department = departmentRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Department with id: %d not found", id)));
        department.setName(departmentDto.getName());
        department.setDescription(departmentDto.getDescription());
        return DepartmentMapper.mapToDepartmentDto(departmentRepository.save(department));
    }

}
