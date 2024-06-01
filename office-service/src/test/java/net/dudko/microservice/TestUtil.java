package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Office;
import net.dudko.microservice.domain.mapper.OfficeMapper;
import net.dudko.microservice.model.dto.OfficeDto;

import java.time.LocalDateTime;

public class TestUtil {

    public static final String MS_NAME = "OFFICE-MICROSERVICE: ";
    private static final LocalDateTime time = LocalDateTime.now();

    public static Office getValidEntity() {
        return Office.builder()
                .id(1L)
                .name("Test Office Name")
                .description("Test Office Description")
                .code("Test Office Code")
                .created(time)
                .build();
    }

    public static OfficeDto getValidDto() {
        return OfficeMapper.mapToOfficeDto(getValidEntity());
    }

}
