package cz.pycrs.bp.backend.security;

import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
@RequiredArgsConstructor
public class PreUpdateSecurityContextInterceptor implements HandlerInterceptor {
    private final UserService userService;

    /**
     * Nasty ass non-scalable hack that on every request hits the DB for the most up-to-date user and puts it in the
     * security context. I don't have to then update every active session's security context after updating
     * a user - I would, but I don't know how and I ain't got time. It's a real bad hack, but I couldn't give fewer
     * fucks about the "sCaLaBiLiTy"
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user)
            SecurityContextHolder.getContext().setAuthentication(
                    new PreAuthenticatedAuthenticationToken(userService.getUser(user.getId().toString()), null, user.getAuthorities()
                    ));
        return true;
    }
}
