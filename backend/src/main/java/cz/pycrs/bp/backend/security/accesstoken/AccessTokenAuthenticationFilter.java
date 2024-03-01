package cz.pycrs.bp.backend.security.accesstoken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String HEADER_NAME = "Authorization";
    private static final String SCHEME = "Bearer";

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Does this request contain the Authorization header?
        if (request.getHeader(HEADER_NAME) == null) {
            log.trace("Did not find Authorization header in request");
            filterChain.doFilter(request, response);
            return;
        }

        var splitHeader = request.getHeader(HEADER_NAME).split(" ");
        if (splitHeader.length != 2 || !splitHeader[0].equals(SCHEME)) {
            log.trace("Authorization header is present, but it's not a Bearer token");
            filterChain.doFilter(request, response);
            return;
        }

        log.trace("Found a Bearer access token");

        var token = splitHeader[1];
        var authentication = authenticationManager.authenticate(
                AccessTokenAuthentication.unauthenticated(token)
        );

        // We have a valid token - set the SecurityContext with the authenticated token
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

}
