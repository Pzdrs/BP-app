package cz.pycrs.bp.backend.payload;

public record UserUpdateRequest(
        String firstName,
        String lastName,
        String password
) {
}
