package cz.pycrs.bp.backend.entity.user;

import cz.pycrs.bp.backend.entity.accesstoken.AccessToken;
import cz.pycrs.bp.backend.security.accesstoken.AccessTokenAuthentication;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Set;

@Document("users")
@Data
@NoArgsConstructor
public class User implements UserDetails {
    @MongoId
    private ObjectId id;
    private String firstName, lastName;
    @Indexed(unique = true)
    private String email;
    private String password;

    private Role role = Role.USER;

    // Only useful if the user is not an administrator
    private Set<String> dataSources = Set.of();
    private Set<String> dataSourceGroups = Set.of();

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date updated;

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password, Role role) {
        this(firstName, lastName, email, password);
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User from(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) return null;
        Object principal = authentication.getPrincipal();
        if(principal instanceof User) return (User) principal;
        if (principal instanceof AccessToken accessToken) return accessToken.getUser();
        return null;
    }
}
