package cz.pycrs.bp.backend.entity.user.dto;

import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;

import java.util.Date;

public record UserProfile(
        String id, String email,
        String firstName, String lastName,
        Role role,
        Date created, Date updated) {
    public UserProfile(User user) {
        this(
                user.getId().toString(), user.getEmail(),
                user.getFirstName(), user.getLastName(),
                user.getRole(),
                user.getCreated(), user.getUpdated()
        );
    }
}
