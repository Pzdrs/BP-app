package cz.pycrs.bp.backend.security.accesstoken;

import cz.pycrs.bp.backend.service.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

public class AccessTokenAuthenticationConfigurer extends AbstractHttpConfigurer<AccessTokenAuthenticationConfigurer, HttpSecurity> {
    private final TokenService tokenService;

    public AccessTokenAuthenticationConfigurer(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void init(HttpSecurity builder) {
        builder.authenticationProvider(new AccessTokenAuthenticationProvider(this.tokenService));
    }

    @Override
    public void configure(HttpSecurity builder) {
        var authenticationManager = builder.getSharedObject(AuthenticationManager.class);
        builder.addFilterBefore(new AccessTokenAuthenticationFilter(authenticationManager), AuthorizationFilter.class);
    }

}
