package cz.pycrs.bp.backend.security.accesstoken;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AccessTokenAuthentication extends AbstractAuthenticationToken {
    private final AccessToken accessToken;
    private final String tokenValue;

    public AccessTokenAuthentication(String tokenValue, AccessToken accessToken) {
        super(null);
        this.accessToken = accessToken;
        this.tokenValue = tokenValue;
    }

    public AccessTokenAuthentication(String tokenValue, AccessToken accessToken, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.accessToken = accessToken;
        this.tokenValue = tokenValue;
        setAuthenticated(true);
    }

    public static AccessTokenAuthentication unauthenticated(String token) {
        return new AccessTokenAuthentication(token, null);
    }

    public static AccessTokenAuthentication authenticated(AccessToken accessToken) {
        return new AccessTokenAuthentication(null, accessToken, accessToken.getUser().getAuthorities());
    }

    @Override
    public Object getCredentials() {
        return this.tokenValue;
    }

    @Override
    public Object getPrincipal() {
        return this.accessToken;
    }

}
