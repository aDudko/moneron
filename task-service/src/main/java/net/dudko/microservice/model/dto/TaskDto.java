package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "Task DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    @Schema(
            description = "Id of task"
    )
    private Long id;

    @Schema(
            description = "Title of task"
    )
    private String title;

    @Schema(
            description = "Description of task"
    )
    private String description;

    @Schema(
            description = "Associated with TaskStatus"
    )
    private TaskStatus status;

    @Schema(
            description = "Code of department to which the employee is assigned"
    )
    private String departmentCode;

    @Schema(
            description = "Code of office to which the employee is assigned"
    )
    private String officeCode;

    @Schema(
            description = "Email of employee"
    )
    private String employeeEmail;

}
