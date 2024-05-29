package net.dudko.microservice.service;

import net.dudko.microservice.model.dto.OfficeDto;

public interface OfficeService {

    OfficeDto create(OfficeDto officeDto);

    OfficeDto getById(Long id);

    OfficeDto getByCode(String code);

}
