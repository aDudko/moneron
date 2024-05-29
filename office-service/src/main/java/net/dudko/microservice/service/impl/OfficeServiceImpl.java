package net.dudko.microservice.service.impl;

import lombok.AllArgsConstructor;
import net.dudko.microservice.domain.mapper.OfficeMapper;
import net.dudko.microservice.domain.repository.OfficeRepository;
import net.dudko.microservice.model.dto.OfficeDto;
import net.dudko.microservice.model.exception.ResourceDuplicatedException;
import net.dudko.microservice.model.exception.ResourceNotFoundException;
import net.dudko.microservice.service.OfficeService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;

    @Override
    public OfficeDto create(OfficeDto officeDto) {
        if (officeRepository.existsByCode(officeDto.getCode())) {
            throw new ResourceDuplicatedException("Code of office already exists");
        }
        var inDb = officeRepository.save(OfficeMapper.mapToOffice(officeDto));
        return OfficeMapper.mapToOfficeDto(inDb);
    }

    @Override
    public OfficeDto getById(Long id) {
        var office = officeRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found with id: " + id));
        return OfficeMapper.mapToOfficeDto(office);
    }

    @Override
    public OfficeDto getByCode(String code) {
        if (!officeRepository.existsByCode(code)) {
            throw new ResourceNotFoundException(String.format("Office with code %s not found!", code));
        }
        var office = officeRepository.findOfficeByCode(code);
        return OfficeMapper.mapToOfficeDto(office);
    }

}
