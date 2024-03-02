package cz.pycrs.bp.backend;

import cz.pycrs.bp.backend.handler.MqttMessageHandler;
import cz.pycrs.bp.backend.payload.AccessTokenIssueRequest;
import cz.pycrs.bp.backend.repository.UserRepository;
import cz.pycrs.bp.backend.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class BackendApplication {
    private final DataSourceService dataSourceService;
    private final DataPointService dataPointService;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup(
            NotificationService notificationService,
            UserService userService,
            UserRepository userRepository,
            TokenService tokenService
    ) {
        return args -> {
/*            tokenService.issueToken(
                    UsernamePasswordAuthenticationToken.authenticated(userRepository.findByEmail("admin@email.cz").get(), null, Set.of()),
                    new AccessTokenIssueRequest("My token", null)
            );*/
            /*notificationService.sendNotification(
                    (User) userService.loadUserByUsername("admin@email.cz"),
                    new Notification(Notification.Severity.INFO,"Info notification", "This is an informative notification.")
            );
            notificationService.sendNotification(
                    (User) userService.loadUserByUsername("admin@email.cz"),
                    new Notification(Notification.Severity.WARNING,"Warning notification", "This is a warning notification.")
            );
            notificationService.sendNotification(
                    (User) userService.loadUserByUsername("admin@email.cz"),
                    new Notification(Notification.Severity.ERROR,"Error notification", "This is an error notification.")
            );*/
            //userService.createUser(new UserRegistrationRequest("admin@email.cz", "admin", "Petr", "Bohac"));
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "inboundMqttChannel")
    public MessageHandler handler() {
        return new MqttMessageHandler(dataSourceService, dataPointService);
    }
}
