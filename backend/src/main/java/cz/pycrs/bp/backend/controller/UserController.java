package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.entity.user.dto.UserProfile;
import cz.pycrs.bp.backend.payload.UserRegistrationRequest;
import cz.pycrs.bp.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationRequest registrationRequest) {
        try {
            return ResponseEntity.ok(userService.createUser(registrationRequest));
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
