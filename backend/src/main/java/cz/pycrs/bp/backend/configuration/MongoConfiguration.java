package cz.pycrs.bp.backend.configuration;

import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.UserRegistrationRequest;
import cz.pycrs.bp.backend.repository.UserRepository;
import cz.pycrs.bp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
@RequiredArgsConstructor
public class MongoConfiguration {
    private final UserRepository userRepository;
    private final UserService userService;

    @Bean
    public CommandLineRunner initialPopulate() {
        return args -> {
            if (userRepository.count() == 0)
                userService.createUser(new UserRegistrationRequest("admin@email.cz", "admin", "The", "Administrator", Role.ADMINISTRATOR));
        };
    }
}
