package cz.pycrs.bp.backend;

import cz.pycrs.bp.backend.handler.MqttMessageHandler;
import cz.pycrs.bp.backend.service.DataPointService;
import cz.pycrs.bp.backend.service.DataSourceService;
import cz.pycrs.bp.backend.service.NotificationService;
import cz.pycrs.bp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.bind.annotation.RestController;

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
    public CommandLineRunner startup(NotificationService notificationService, UserService userService) {
        return args -> {
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
