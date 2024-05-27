package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "Department DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    @Schema(
            description = "Id of department"
    )
    private Long id;

    @Schema(
            description = "Name of department"
    )
    private String name;

    @Schema(
            description = "Description of department"
    )
    private String description;

    @Schema(
            description = "Code of department"
    )
    private String code;

}
