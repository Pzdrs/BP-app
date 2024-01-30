package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

    private final SessionRegistry sessionRegistry;

    @Override
    public void expireUserSessions(User user) {
        getUserSessions(user, false).forEach(SessionInformation::expireNow);
    }

    @Override
    public List<SessionInformation> getUserSessions(User user, boolean includeExpiredSessions) {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof User)
                .map(principal -> (User) principal)
                .filter(principal -> principal.getId().equals(user.getId()))
                .flatMap(principal -> sessionRegistry.getAllSessions(principal, includeExpiredSessions).stream())
                .toList();
    }
}
