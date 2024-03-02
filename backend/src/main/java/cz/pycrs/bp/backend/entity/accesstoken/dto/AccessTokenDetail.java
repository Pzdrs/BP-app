package cz.pycrs.bp.backend.entity.accesstoken.dto;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import cz.pycrs.bp.backend.entity.user.dto.UserProfile;

import java.time.LocalDateTime;

public record AccessTokenDetail(
        String id,
        String value,
        UserProfile user,
        LocalDateTime created,
        LocalDateTime expiry,
        String description,
        boolean disabled
) {
    public AccessTokenDetail(AccessToken accessToken) {
        this(
                accessToken.getId().toString(),
                accessToken.getValue(),
                new UserProfile(accessToken.getUser()),
                accessToken.getCreated(),
                accessToken.getExpiry(),
                accessToken.getDescription(),
                accessToken.isDisabled());
    }
}
