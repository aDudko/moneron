package net.dudko.microservice.service.impl;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.StaffMapper;
import net.dudko.microservice.domain.repository.StaffRepository;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.DepartmentDto;
import net.dudko.microservice.model.dto.OfficeDto;
import net.dudko.microservice.model.dto.StaffDto;
import net.dudko.microservice.model.dto.StaffStatus;
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
    public StaffDto create(StaffDto staffDto) {
        if (staffRepository.existsByEmail(staffDto.getEmail())) {
            throw new ResourceDuplicatedException(String.format("Employee with email:%s already exists!", staffDto.getEmail()));
        }
        staffDto.setStatus(StaffStatus.CREATED);
        var inDb = staffRepository.save(StaffMapper.mapToStaff(staffDto));
        return StaffMapper.mapToStaffDto(inDb);
    }

    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultResponse")
    @Override
    public ApiResponseDto getById(Long id) {
        var inDb = staffRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found!", id)));
        var inDepartmentService = departmentServiceApiClient.getDepartmentByCode(inDb.getDepartmentCode());
        var inOfficeService = officeServiceApiClient.getOfficeByCode(inDb.getOfficeCode());
        return ApiResponseDto.builder()
                .staffDto(StaffMapper.mapToStaffDto(inDb))
                .departmentDto(inDepartmentService)
                .officeDto(inOfficeService)
                .build();
    }

    @Override
    public List<StaffDto> getAll() {
        return staffRepository.findAll().stream().map(StaffMapper::mapToStaffDto).toList();
    }

    @Override
    public StaffDto update(Long id, StaffDto staffDto) {
        staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found!", id)));
        var inDb = staffRepository.save(StaffMapper.mapToStaff(staffDto));
        return StaffMapper.mapToStaffDto(inDb);
    }

    @Override
    public void delete(Long id) {
        var inDb = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found!", id)));
        inDb.setStatus(StaffStatus.DELETED);
        staffRepository.save(inDb);
    }

    public ApiResponseDto getDefaultResponse(Long id, Exception exception) {
        var inDb = staffRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found!", id)));
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
                .staffDto(StaffMapper.mapToStaffDto(inDb))
                .departmentDto(inDepartmentService)
                .officeDto(inOfficeService)
                .build();
    }

}
