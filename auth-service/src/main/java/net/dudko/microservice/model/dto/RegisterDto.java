package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "Register DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

        @Schema(
                description = "User holder name"
        )
        private String name;

        @Schema(
                description = "Login of user"
        )
        private String username;

        @Schema(
                description = "Email of user"
        )
        private String email;

        @Schema(
                description = "Password of user"
        )
        private String password;

}
