package cz.pycrs.bp.backend.entity.user.dto;

import cz.pycrs.bp.backend.entity.user.User;

public record UserProfile(String username, String email) {
    public UserProfile(User user) {
        this(user.getUsername(), user.getEmail());
    }
}
