package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.entity.user.dto.UserProfile;
import cz.pycrs.bp.backend.repository.UserRepository;
import cz.pycrs.bp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfile createUser(String username, String email, String password) {
        User user = userRepository.save(new User(username, email, passwordEncoder.encode(password)));
        return new UserProfile(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> byUsername = userRepository.findByUsername(username);
        Optional<User> byEmail = userRepository.findByEmail(username);

        if (byUsername.isPresent()) {
            return byUsername.get();
        } else if (byEmail.isPresent()) {
            return byEmail.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
