package cz.pycrs.bp.backend.payload;

import java.time.LocalDateTime;

public record AccessTokenUpdateRequest(
        String description,
        LocalDateTime expiry,
        boolean disabled
) {
}
