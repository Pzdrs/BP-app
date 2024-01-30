package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private static final Log logger = LogFactory.getLog(SessionServiceImpl.class);

    private final SessionRegistry sessionRegistry;

    @Override
    public void expireUserSessions(User user) {
        getUserSessions(user, false).forEach(session -> {
            logger.info(LogMessage.format("Expiring session %s for user %s", session.getSessionId(), user.getId()));
            session.expireNow();
        });
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
