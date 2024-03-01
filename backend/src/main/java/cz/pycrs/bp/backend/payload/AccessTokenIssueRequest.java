package cz.pycrs.bp.backend.payload;

import java.time.LocalDateTime;

public record AccessTokenIssueRequest(
        String description,
        LocalDateTime expiry
) {
}
