package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.entity.user.dto.UserProfile;
import cz.pycrs.bp.backend.payload.UserRegistrationRequest;
import cz.pycrs.bp.backend.payload.UserUpdateRequest;
import cz.pycrs.bp.backend.repository.UserRepository;
import cz.pycrs.bp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("User with email " + request.email() + " already exists");
        }

        return userRepository.save(
                new User(
                        request.firstName(),
                        request.lastName(),
                        request.email(),
                        passwordEncoder.encode(request.password()),
                        request.role() == null ? Role.getDefault() : request.role()
                )
        );
    }

    @Override
    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getAllAdministrators() {
        return userRepository.findAllByRole(Role.ADMINISTRATOR);
    }

    @Override
    public List<Role> getAllRoles() {
        return List.of(Role.values());
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public User updateUser(String id, UserUpdateRequest request, Authentication authentication) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            if (!request.password().isBlank()) user.setPassword(passwordEncoder.encode(request.password()));
            if (request.role() != null) user.setRole(request.role());
            User updatedUser = userRepository.save(user);

            // If the updated user is the currently authenticated user, update the principal in the security context
            if (((User) authentication.getPrincipal()).getId().equals(updatedUser.getId())) {
                SecurityContextHolder.getContext().setAuthentication(
                        new PreAuthenticatedAuthenticationToken(updatedUser, null, updatedUser.getAuthorities())
                );
            }
            return updatedUser;
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
