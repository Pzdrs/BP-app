package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.user.dto.UserProfile;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserProfile createUser(String username, String email, String password);
}
