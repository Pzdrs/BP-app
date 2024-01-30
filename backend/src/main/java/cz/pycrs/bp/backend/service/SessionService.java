package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.entity.user.User;
import org.springframework.security.core.session.SessionInformation;

import java.util.List;

public interface SessionService {
    void expireUserSessions(User user);

    List<SessionInformation> getUserSessions(User user, boolean includeExpiredSessions);
}
