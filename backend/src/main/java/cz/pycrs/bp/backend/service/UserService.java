package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.entity.user.dto.UserProfile;
import cz.pycrs.bp.backend.payload.UserRegistrationRequest;
import cz.pycrs.bp.backend.payload.UserUpdateRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User createUser(UserRegistrationRequest request);

    User getUser(String id);

    List<User> getAllUsers();

    List<Role> getAllRoles();

    void deleteUser(String id);

    User updateUser(String id, UserUpdateRequest request, Authentication authentication);

}
