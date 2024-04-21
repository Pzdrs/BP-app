package cz.pycrs.bp.backend.configuration;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.security.JsonUsernamePasswordAuthenticationFilter;
import cz.pycrs.bp.backend.security.PreUpdateSecurityContextInterceptor;
import cz.pycrs.bp.backend.security.RestfulAuthenticationEntryPoint;
import cz.pycrs.bp.backend.security.accesstoken.AccessTokenAuthenticationConfigurer;
import cz.pycrs.bp.backend.service.TokenService;
import cz.pycrs.bp.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration implements WebMvcConfigurer {
    private final AuthenticationManager authenticationManager;

    private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
    private final TokenService tokenService;
    private final UserService userService;

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

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
                        for (String origin : allowedOrigins.split(",")) {
                            corsConfiguration.addAllowedOrigin(origin);
                        }
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
                    authorize.requestMatchers(request ->
                                    request.getServletPath().startsWith("/datasource/") && !HttpMethod.GET.matches(request.getMethod()))
                            .hasAnyRole(Role.ADMINISTRATOR.name());

                    authorize.requestMatchers(HttpMethod.GET, "/datapoint/**").authenticated();
                    authorize.requestMatchers(request ->
                                    request.getServletPath().startsWith("/datapoint/") && !HttpMethod.GET.matches(request.getMethod()))
                            .hasAnyRole(Role.ADMINISTRATOR.name());

                    // Only administrators can alter the configuration, everyone else can read it tho
                    authorize.requestMatchers("/config/**").authenticated();
                    authorize.requestMatchers(HttpMethod.POST, "/config/**").hasAnyRole(Role.ADMINISTRATOR.name());

                    // Any authenticated user can access their own profile
                    authorize.requestMatchers("/user/me").authenticated();
                    // Users can modify their own profile only, administrators can modify any profile
                    authorize.requestMatchers(HttpMethod.PATCH, "/user/{id}").access((authentication, object) -> {
                        Authentication auth = authentication.get();
                        return new AuthorizationDecision(
                                auth.getAuthorities().contains(Role.ADMINISTRATOR.getAuthority())
                                        || (User.from(auth)).getId().toString().equals(object.getVariables().get("id"))
                        );
                    });
                    // Anything else in the user namespace is only accessible to administrators
                    authorize.requestMatchers("/user/**").hasRole(Role.ADMINISTRATOR.name());

                    authorize.requestMatchers("/notification/**").authenticated();

                    authorize.requestMatchers(HttpMethod.GET, "/token/**").authenticated();
                    authorize.requestMatchers("/token/issue").authenticated();
                    authorize.requestMatchers("/token/{id}").access((authentication, object) -> {
                        Authentication auth = authentication.get();
                        System.out.println(object.getVariables().get("id"));
                        var token = ((AccessToken) tokenService.getAccessToken(object.getVariables().get("id")));
                        System.out.println(token);
                        return new AuthorizationDecision(
                                auth.getAuthorities().contains(Role.ADMINISTRATOR.getAuthority())
                                        || (User.from(auth)).getId().equals(token.getUser().getId())
                        );
                    });


                    authorize.anyRequest().denyAll();
                })
                .logout(logout -> {
                    logout.logoutUrl("/auth/logout");
                    logout.deleteCookies("JSESSIONID");
                    logout.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
                })
                .addFilterAt(new JsonUsernamePasswordAuthenticationFilter(authenticationManager, sessionAuthenticationStrategy), UsernamePasswordAuthenticationFilter.class)
                .with(new AccessTokenAuthenticationConfigurer(tokenService), withDefaults())
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint(new RestfulAuthenticationEntryPoint());
                })
                .build();
    }

}
