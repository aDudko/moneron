package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.ApiResponseDto;
import net.dudko.microservice.model.dto.StaffDto;

import java.util.List;

public interface StaffService {

    StaffDto create(StaffDto staffDto);

    ApiResponseDto getById(Long id);

    StaffDto getByEmail(String email);

    List<StaffDto> getAll();

    StaffDto update(Long id, StaffDto staffDto);

    void delete(Long id);

}
