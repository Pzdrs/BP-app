package cz.pycrs.bp.backend.payload;

import cz.pycrs.bp.backend.entity.user.Role;

public record UserUpdateRequest(
        String firstName,
        String lastName,
        String password,

        Role role
) {
}
