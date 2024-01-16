package cz.pycrs.bp.backend.payload;

import cz.pycrs.bp.backend.entity.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegistrationRequest(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password,
        String firstName,
        String lastName,
        Role role
) {
}
