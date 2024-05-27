package net.dudko.microservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        description = "Login DTO (Data Transfer Object) to transfer the data between client and server"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

        @Schema(
                description = "Username or Email"
        )
        private String usernameOrEmail;

        @Schema(
                description = "Password"
        )
        private String password;

}
