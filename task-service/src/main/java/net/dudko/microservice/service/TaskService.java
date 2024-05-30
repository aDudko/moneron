package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.TaskDto;

import java.util.List;

public interface TaskService {

    TaskDto create(TaskDto taskDto);

    ApiResponseDto getById(Long id);

    List<TaskDto> getAllByEmployeeEmail(String email);

    TaskDto update(Long id, TaskDto taskDto);

    void delete(Long id);

}
