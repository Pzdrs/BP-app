package cz.pycrs.bp.backend.controller;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import cz.pycrs.bp.backend.entity.accesstoken.dto.AccessTokenDetail;
import cz.pycrs.bp.backend.entity.datasource.DataSource;
import cz.pycrs.bp.backend.entity.user.Role;
import cz.pycrs.bp.backend.entity.user.User;
import cz.pycrs.bp.backend.payload.AccessTokenIssueRequest;
import cz.pycrs.bp.backend.payload.AccessTokenUpdateRequest;
import cz.pycrs.bp.backend.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
public class AccessTokenController {
    private final TokenService tokenService;

    @GetMapping("/all")
    public Set<AccessTokenDetail> all(Authentication authentication) {
        return tokenService.getAllTokens(authentication).stream()
                .map(token -> new AccessTokenDetail(((AccessToken) token)))
                .collect(Collectors.toSet());
    }

    @PostMapping("/issue")
    public AccessTokenDetail issue(Authentication authentication, @RequestBody AccessTokenIssueRequest request) {
        return new AccessTokenDetail(((AccessToken) tokenService.issueToken(authentication, request)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tokenService.revokeToken(id);
    }

    @PatchMapping("/{id}")
    public AccessTokenDetail update(@PathVariable String id, @RequestBody AccessTokenUpdateRequest request) {
        return new AccessTokenDetail(((AccessToken) tokenService.updateToken(id, request)));
    }
}
