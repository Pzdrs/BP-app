package cz.pycrs.bp.backend.payload;

import cz.pycrs.bp.backend.entity.user.Role;

import java.util.Set;

public record UserUpdateRequest(
        String firstName,
        String lastName,
        String password,
        Role role,
        Set<String> dataSources, Set<String> dataSourceGroups
) {
}
