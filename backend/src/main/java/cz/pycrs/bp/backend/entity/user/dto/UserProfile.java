package cz.pycrs.bp.backend.entity.user.dto;

import cz.pycrs.bp.backend.entity.user.User;

public record UserProfile(String id, String email, String firstName, String lastName) {
    public UserProfile(User user) {
        this(user.getId().toString(), user.getEmail(), user.getFirstName(), user.getLastName());
    }
}
