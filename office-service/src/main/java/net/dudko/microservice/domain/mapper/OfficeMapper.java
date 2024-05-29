package net.dudko.microservice.domain.mapper;

import net.dudko.microservice.domain.entity.Office;
import net.dudko.microservice.model.dto.OfficeDto;

public class OfficeMapper {

    public static Office mapToOffice(OfficeDto officeDto) {
        return Office.builder()
                .id(officeDto.getId())
                .name(officeDto.getName())
                .description(officeDto.getDescription())
                .code(officeDto.getCode())
                .created(officeDto.getCreated())
                .build();
    }

    public static OfficeDto mapToOfficeDto(Office office) {
        return OfficeDto.builder()
                .id(office.getId())
                .name(office.getName())
                .description(office.getDescription())
                .code(office.getCode())
                .created(office.getCreated())
                .build();
    }

}
