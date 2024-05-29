package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(
        description = "Office DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfficeDto {

    @Schema(
            description = "Id of office"
    )
    private Long id;

    @Schema(
            description = "Name of office"
    )
    private String name;

    @Schema(
            description = "Description of office"
    )
    private String description;

    @Schema(
            description = "Code of office"
    )
    private String code;

    @Schema(
            description = "Date and time the office was created"
    )
    private LocalDateTime created;

}
