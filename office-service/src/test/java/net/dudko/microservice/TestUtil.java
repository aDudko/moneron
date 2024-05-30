package net.dudko.microservice;

import net.dudko.microservice.domain.entity.Office;
import net.dudko.microservice.model.dto.OfficeDto;

import java.time.LocalDateTime;

public class TestUtil {

    private static final LocalDateTime time = LocalDateTime.now();

    public static Office getValidEntity() {
        return Office.builder()
                .id(1L)
                .name("Test Office Default. Name")
                .description("Test Office Default. Description")
                .code("TOD")
                .created(time)
                .build();
    }

    public static OfficeDto getValidDto() {
        return OfficeDto.builder()
                .id(1L)
                .name("Test Office Default. Name")
                .description("Test Office Default. Description")
                .code("TOD")
                .created(time)
                .build();
    }

}
