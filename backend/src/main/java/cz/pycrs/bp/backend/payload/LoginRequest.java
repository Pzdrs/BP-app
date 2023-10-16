package cz.pycrs.bp.backend.payload;

public record LoginRequest(
        String username, String password
) {
}
