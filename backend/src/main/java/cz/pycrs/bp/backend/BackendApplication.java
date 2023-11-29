package cz.pycrs.bp.backend;

import cz.pycrs.bp.backend.entity.ApplicationConfiguration;
import cz.pycrs.bp.backend.entity.configuration.MqttConfiguration;
import cz.pycrs.bp.backend.repository.ApplicationConfigurationRepository;
import cz.pycrs.bp.backend.service.ApplicationConfigurationService;
import cz.pycrs.bp.backend.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequiredArgsConstructor
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @GetMapping
    public String hello(Authentication authentication, HttpSession session) {
        System.out.println(authentication);
        System.out.println(session.getId());
        session.getAttributeNames().asIterator().forEachRemaining(System.out::println);
        System.out.println(session.getServletContext());
        return "Hello world!";
    }

    @GetMapping("/secure")
    public String secure(Authentication authentication) {
        System.out.println(authentication);
        return "Hello world!";
    }


    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            System.out.println(userService.loadUserByUsername("user"));
        };
    }

    @Bean
    @ServiceActivator(inputChannel = "inboundMqtt")
    public MessageHandler handler() {
        return message -> System.out.println(message.getPayload());
    }
}
