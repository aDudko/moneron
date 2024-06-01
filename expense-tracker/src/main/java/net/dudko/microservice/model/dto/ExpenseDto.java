package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(
        description = "Expense DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {

    @Schema(
            description = "Id of expense"
    )
    private Long id;

    @Schema(
            description = "Amount of expense"
    )
    private BigDecimal amount;

    @Schema(
            description = "Created date of expense"
    )
    private LocalDate expenseDate;

    @Schema(
            description = "Name of category to witch the category is assigned"
    )
    private String categoryName;
}
