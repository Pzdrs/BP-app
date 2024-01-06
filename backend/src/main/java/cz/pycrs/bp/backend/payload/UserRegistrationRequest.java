package cz.pycrs.bp.backend.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password,
        String firstName,
        String lastName
) {
}
