package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "Category DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @Schema(
            description = "Id of category"
    )
    private Long id;

    @Schema(
            description = "Name of category"
    )
    private String name;

}
