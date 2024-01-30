package cz.pycrs.bp.backend.configuration;

import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.security.JsonUsernamePasswordAuthenticationFilter;
import cz.pycrs.bp.backend.security.PreUpdateSecurityContextInterceptor;
import cz.pycrs.bp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration implements WebMvcConfigurer {
    private final AuthenticationManager authenticationManager;

    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new PreUpdateSecurityContextInterceptor(userService));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> {
                    //csrf.csrfTokenRepository(new CookieCsrfTokenRepository());
                    csrf.disable();
                })
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        var corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOrigin("http://localhost:5173");
                        corsConfiguration.addAllowedMethod("*");
                        corsConfiguration.addAllowedHeader("*");
                        corsConfiguration.setAllowCredentials(true);
                        return corsConfiguration;
                    });
                })
                .authorizeHttpRequests(authorize -> {
                    // Authentication needs no authorization - duh
                    authorize.requestMatchers("/auth/**").permitAll();

                    authorize.requestMatchers(HttpMethod.GET, "/datasource/**").authenticated();
                    authorize.requestMatchers(request -> !HttpMethod.GET.matches(request.getMethod())).hasAnyRole(Role.ADMINISTRATOR.name());

                    // Only administrators can alter the configuration, everyone else can read it tho
                    authorize.requestMatchers("/config/**").authenticated();
                    authorize.requestMatchers(HttpMethod.POST, "/config/**").hasAnyRole(Role.ADMINISTRATOR.name());

                    authorize.requestMatchers("/user/me").authenticated();
                    authorize.requestMatchers("/user/**").hasRole(Role.ADMINISTRATOR.name());

                    authorize.requestMatchers("/notification/**").authenticated();

                    authorize.anyRequest().denyAll();
                })
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");
                    logout.deleteCookies("JSESSIONID");
                    logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
                })
                .addFilterAt(new JsonUsernamePasswordAuthenticationFilter(authenticationManager, sessionAuthenticationStrategy), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
