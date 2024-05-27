package net.dudko.microservice.model.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        description = "ErrorDetails DTO (Data Transfer Object) to transfer " +
                "the error response data between client and server"
)
@Data
@Builder
public class ErrorDetails {

    @Schema(
            description = "Error occurred date time"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "Error message"
    )
    private String message;

    @Schema(
            description = "Error details"
    )
    private String details;

    @Schema(
            description = "Error code"
    )
    private String errorCode;

}
