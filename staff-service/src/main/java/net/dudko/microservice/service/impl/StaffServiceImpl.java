package net.dudko.microservice.service.impl;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.EmployeeMapper;
import net.dudko.microservice.domain.repository.StaffRepository;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.DepartmentDto;
import net.dudko.microservice.model.dto.OfficeDto;
import net.dudko.microservice.model.dto.EmployeeDto;
import net.dudko.microservice.model.dto.EmployeeStatus;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.DepartmentServiceApiClient;
import net.dudko.microservice.service.OfficeServiceApiClient;
import net.dudko.microservice.service.StaffService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    private final DepartmentServiceApiClient departmentServiceApiClient;

    private final OfficeServiceApiClient officeServiceApiClient;

    @Override
    public EmployeeDto create(EmployeeDto staffDto) {
        if (staffRepository.existsByEmail(staffDto.getEmail())) {
            throw new ResourceDuplicatedException(String.format("Employee with email: %s already exists!", staffDto.getEmail()));
        }
        staffDto.setStatus(EmployeeStatus.CREATED);
        var inDb = staffRepository.save(EmployeeMapper.mapToEmployee(staffDto));
        return EmployeeMapper.mapToEmployeeDto(inDb);
    }

    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultResponse")
    @Override
    public ApiResponseDto getById(Long id) {
        var inDb = staffRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id: %d not found", id)));
        var inDepartmentService = departmentServiceApiClient.getDepartmentByCode(inDb.getDepartmentCode());
        var inOfficeService = officeServiceApiClient.getOfficeByCode(inDb.getOfficeCode());
        return ApiResponseDto.builder()
                .employeeDto(EmployeeMapper.mapToEmployeeDto(inDb))
                .departmentDto(inDepartmentService)
                .officeDto(inOfficeService)
                .build();
    }

    @Override
    public EmployeeDto getByEmail(String email) {
        if (!staffRepository.existsByEmail(email)) {
            throw new ResourceNotFoundException(String.format("Employee with email: %s not found", email));
        }
        var inDb = staffRepository.findByEmail(email);
        return EmployeeMapper.mapToEmployeeDto(inDb);
    }

    @Override
    public List<EmployeeDto> getStaff() {
        return staffRepository.findAll().stream().map(EmployeeMapper::mapToEmployeeDto).toList();
    }

    @Override
    public EmployeeDto update(Long id, EmployeeDto staffDto) {
        staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id: %d not found", id)));
        var inDb = staffRepository.save(EmployeeMapper.mapToEmployee(staffDto));
        return EmployeeMapper.mapToEmployeeDto(inDb);
    }

    @Override
    public void delete(Long id) {
        var inDb = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id: %d not found", id)));
        inDb.setStatus(EmployeeStatus.DELETED);
        staffRepository.save(inDb);
    }

    public ApiResponseDto getDefaultResponse(Long id, Exception exception) {
        var inDb = staffRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id: %d not found", id)));
        var inDepartmentService = DepartmentDto.builder()
                .name("Default Department")
                .description("You see this department, because department-service not available. Contact Support")
                .code("DF")
                .build();
        var inOfficeService = OfficeDto.builder()
                .name("Default Office")
                .description("You see this office, because office-service not available. Contact Support")
                .code("DF")
                .created(LocalDateTime.now())
                .build();
        return ApiResponseDto.builder()
                .employeeDto(EmployeeMapper.mapToEmployeeDto(inDb))
                .departmentDto(inDepartmentService)
                .officeDto(inOfficeService)
                .build();
    }

}
