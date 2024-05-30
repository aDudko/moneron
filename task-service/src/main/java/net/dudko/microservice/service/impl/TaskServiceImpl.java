package net.dudko.microservice.service.impl;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.TaskMapper;
import net.dudko.microservice.domain.repository.TaskRepository;
import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.DepartmentDto;
import net.dudko.microservice.model.dto.OfficeDto;
import net.dudko.microservice.model.dto.StaffDto;
import net.dudko.microservice.model.dto.StaffStatus;
import net.dudko.microservice.model.dto.TaskDto;
import net.dudko.microservice.model.dto.TaskStatus;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.DepartmentServiceApiClient;
import net.dudko.microservice.service.OfficeServiceApiClient;
import net.dudko.microservice.service.StaffServiceApiClient;
import net.dudko.microservice.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final DepartmentServiceApiClient departmentServiceApiClient;

    private final OfficeServiceApiClient officeServiceApiClient;

    private final StaffServiceApiClient staffServiceApiClient;

    @Override
    public TaskDto create(TaskDto taskDto) {
        taskDto.setStatus(TaskStatus.TODO);
        var inDb = taskRepository.save(TaskMapper.mapToTask(taskDto));
        return TaskMapper.mapToTaskDto(inDb);
    }

    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultResponse")
    @Override
    public ApiResponseDto getById(Long id) {
        var inDb = taskRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id '%d' not found", id)));
        var inDepartmentService = departmentServiceApiClient.getDepartmentByCode(inDb.getDepartmentCode());
        var inOfficeService = officeServiceApiClient.getOfficeByCode(inDb.getOfficeCode());
        var inStaffService = staffServiceApiClient.getEmployeeByEmail(inDb.getEmployeeEmail());
        return ApiResponseDto.builder()
                .taskDto(TaskMapper.mapToTaskDto(inDb))
                .departmentDto(inDepartmentService)
                .officeDto(inOfficeService)
                .staffDto(inStaffService)
                .build();
    }

    @Override
    public List<TaskDto> getAllByEmployeeEmail(String email) {
        return taskRepository.findAllByEmployeeEmail(email).stream().map(TaskMapper::mapToTaskDto).toList();
    }

    @Override
    public TaskDto update(Long id, TaskDto taskDto) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id '%d' not found", id)));
        var inDb = taskRepository.save(TaskMapper.mapToTask(taskDto));
        return TaskMapper.mapToTaskDto(inDb);
    }

    @Override
    public void delete(Long id) {
        taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id '%d' not found", id)));
        taskRepository.deleteById(id);
    }

    public ApiResponseDto getDefaultResponse(Long id, Exception exception) {
        var inDb = taskRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Task with id %s not found!", id)));
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
        var inStaffService = StaffDto.builder()
                .firstName("Default First Name")
                .lastName("Default Last Name")
                .email("You see this email, because staff-service not available. Contact Support")
                .status(StaffStatus.DELETED)
                .departmentCode(inDepartmentService.getCode())
                .officeCode(inOfficeService.getCode())
                .build();
        return ApiResponseDto.builder()
                .taskDto(TaskMapper.mapToTaskDto(inDb))
                .departmentDto(inDepartmentService)
                .officeDto(inOfficeService)
                .staffDto(inStaffService)
                .build();
    }

}
