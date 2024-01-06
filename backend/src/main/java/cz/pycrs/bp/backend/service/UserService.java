package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.user.dto.UserProfile;
import cz.pycrs.bp.backend.payload.UserRegistrationRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserProfile createUser(UserRegistrationRequest request);
}
