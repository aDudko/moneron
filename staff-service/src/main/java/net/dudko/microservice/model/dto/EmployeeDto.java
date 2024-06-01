package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(
        description = "Employee DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @Schema(
            description = "Id of employee"
    )
    private Long id;

    @Schema(
            description = "First name of employee"
    )
    private String firstName;

    @Schema(
            description = "Last name of employee"
    )
    private String lastName;

    @Schema(
            description = "Email of employee"
    )
    private String email;

    @Schema(
            description = "Status of employee"
    )
    private EmployeeStatus status;

    @Schema(
            description = "Code of department to which the employee is assigned"
    )
    private String departmentCode;

    @Schema(
            description = "Code of office to which the employee is assigned"
    )
    private String officeCode;

}
