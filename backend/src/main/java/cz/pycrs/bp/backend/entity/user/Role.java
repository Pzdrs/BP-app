package cz.pycrs.bp.backend.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    ADMINISTRATOR(Collections.emptySet());

    @Getter
    private final Set<GrantedAuthority> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> list = new ArrayList<>(getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getAuthority())).toList());
        list.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return list;
    }

    public static Role getDefault() {
        return USER;
    }
}
