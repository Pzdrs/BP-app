package cz.pycrs.bp.backend.security.accesstoken;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import cz.pycrs.bp.backend.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var authRequest = (AccessTokenAuthentication) authentication;
        var token = ((AccessToken) tokenService.getAccessToken(authRequest.getCredentials().toString()));

        if (token == null) throw new BadCredentialsException("Invalid token");
        if (token.isExpired()) throw new BadCredentialsException("Token is expired");
        if (token.isDisabled()) throw new DisabledException("Token is disabled");

        return AccessTokenAuthentication.authenticated(token);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenAuthentication.class.isAssignableFrom(authentication);
    }

}
