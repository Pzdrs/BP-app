package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.entity.user.dto.UserProfile;
import cz.pycrs.bp.backend.payload.UserRegistrationRequest;
import cz.pycrs.bp.backend.payload.UserUpdateRequest;
import cz.pycrs.bp.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public List<UserProfile> allUsers(Authentication authentication) {
        return userService
                .getAllUsers()
                .stream()
                .sorted((u1, u2) -> {
                    String authenticatedId = ((User) authentication.getPrincipal()).getId().toString();
                    if (u1.getId().toString().equals(authenticatedId)) {
                        return -1;
                    }
                    if (u2.getId().toString().equals(authenticatedId)) {
                        return 1;
                    }
                    return u1.getLastName().compareTo(u2.getLastName());
                })
                .map(UserProfile::new).toList();
    }

    @GetMapping("/roles")
    public List<Role> allRoles() {
        return userService.getAllRoles();
    }

    @GetMapping("/{id}")
    public UserProfile userProfile(@PathVariable String id) {
        return new UserProfile(userService.getUser(id));
    }

    @PatchMapping("/{id}")
    public UserProfile updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request, Authentication authentication) {
        return new UserProfile(userService.updateUser(id, request, authentication));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        try {
            return ResponseEntity.ok(new UserProfile(userService.createUser(registrationRequest)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfile> userProfile(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok(new UserProfile((User) authentication.getPrincipal()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
