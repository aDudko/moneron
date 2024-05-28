package net.dudko.microservice.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.StaffMapper;
import net.dudko.microservice.domain.repository.StaffRepository;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.StaffDto;
import net.dudko.microservice.model.dto.StaffStatus;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.DepartmentServiceApiClient;
import net.dudko.microservice.service.StaffService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@AllArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

//    private final RestTemplate restTemplate;
//    private final WebClient webClient;
    private final DepartmentServiceApiClient departmentServiceApiClient;

    @Override
    public StaffDto create(StaffDto staffDto) {
        if (staffRepository.existsByEmail(staffDto.getEmail())) {
            throw new ResourceDuplicatedException(String.format("Employee with email:%s already exists!", staffDto.getEmail()));
        }
        staffDto.setStatus(StaffStatus.CREATED);
        var inDb = staffRepository.save(StaffMapper.mapToStaff(staffDto));
        return StaffMapper.mapToStaffDto(inDb);
    }

    @Override
    public ApiResponseDto getById(Long id) {
        var inDb = staffRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Employee with id %s not found!", id)));

//        // Call department-service with restTemplate
//        var inDepartmentService = restTemplate.getForEntity(
//                "http://localhost:8081/department/code/" + inDb.getDepartmentCode(),
//                DepartmentDto.class);

//        // Call department-service with webClient
//        var inDepartmentService = webClient.get()
//                .uri( "http://localhost:8081/department/code/" + inDb.getDepartmentCode())
//                .retrieve()
//                .bodyToMono(DepartmentDto.class)
//                .block();

        // Call department-service with openFeign
        var inDepartmentService = departmentServiceApiClient.getDepartmentByCode(inDb.getDepartmentCode());

        return ApiResponseDto.builder()
                .staffDto(StaffMapper.mapToStaffDto(inDb))
//                .departmentDto(inDepartmentService.getBody())
                .departmentDto(inDepartmentService)
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

}
