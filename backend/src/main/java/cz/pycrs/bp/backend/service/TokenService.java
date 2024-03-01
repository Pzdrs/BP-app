package cz.pycrs.bp.backend.service;

import cz.pycrs.bp.backend.payload.AccessTokenIssueRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Token;

import java.util.Set;

public interface TokenService {
    Token getAccessToken(String key);

    Set<Token> getAllTokens();
    Token issueToken(Authentication authentication, AccessTokenIssueRequest request);
}
