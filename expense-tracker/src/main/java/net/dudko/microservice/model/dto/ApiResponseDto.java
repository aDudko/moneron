package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "ApiResponse DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto {

    @Schema(
            description = "Associate with ExpenseDto"
    )
    private ExpenseDto expenseDto;

    @Schema(
            description = "Associate with CategoryDto"
    )
    private CategoryDto categoryDto;

}
