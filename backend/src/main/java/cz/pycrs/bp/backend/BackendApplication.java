package cz.pycrs.bp.backend;

import jakarta.servlet.http.HttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(
        exclude = {
                DataSourceAutoConfiguration.class
        }
)
@RestController
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
}
