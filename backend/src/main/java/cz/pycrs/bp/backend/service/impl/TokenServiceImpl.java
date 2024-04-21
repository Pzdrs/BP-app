package cz.pycrs.bp.backend.service.impl;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.AccessTokenIssueRequest;
import cz.pycrs.bp.backend.payload.AccessTokenUpdateRequest;
import cz.pycrs.bp.backend.repository.AccessTokenRepository;
import cz.pycrs.bp.backend.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.Token;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final AccessTokenRepository accessTokenRepository;

    public static final BiPredicate<AccessToken, Object> IS_VISIBLE_TO_USER = (token, authentication) -> {
        var user = User.from((Authentication) authentication);
        return user.getRole() == Role.ADMINISTRATOR || token.getUser().getId().equals(user.getId());
    };


    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe
    private static final int TOKEN_LENGTH = 32;

    @Override
    public Token getAccessToken(String valueOrId) {
        Optional<AccessToken> byValue = accessTokenRepository.findByValue(valueOrId);
        return byValue.orElseGet(() -> accessTokenRepository.findById(valueOrId).orElse(null));
    }

    @Override
    public Set<Token> getAllTokens(Authentication authentication) {
        return Set.copyOf(accessTokenRepository.findAll().stream()
                .filter(token -> IS_VISIBLE_TO_USER.test(token, authentication)).collect(Collectors.toSet())
        );
    }

    @Override
    public Token issueToken(Authentication authentication, AccessTokenIssueRequest request) {
        if (authentication.getPrincipal() instanceof AccessToken)
            throw new IllegalArgumentException("Cannot issue token for token");
        var user = (User) authentication.getPrincipal();
        var accessToken = AccessToken.builder()
                .value(generateToken())
                .expiry(request.expiry())
                .created(LocalDateTime.now())
                .description(request.description())
                .user(user)
                .build();
        return accessTokenRepository.save(accessToken);
    }

    @Override
    public Token updateToken(String id, AccessTokenUpdateRequest request) {
        var accessToken = accessTokenRepository.findById(id).orElseThrow();
        accessToken.setDescription(request.description());
        accessToken.setExpiry(request.expiry());
        accessToken.setDisabled(request.disabled());
        return accessTokenRepository.save(accessToken);
    }

    @Override
    public void revokeToken(String id) {
        accessTokenRepository.deleteById(id);
    }


    public static String generateToken() {
        byte[] randomBytes = new byte[TOKEN_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
