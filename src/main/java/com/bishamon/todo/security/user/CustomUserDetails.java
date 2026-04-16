package com.bishamon.todo.security.user;

import com.bishamon.todo.entity.User;
import com.bishamon.todo.enumeration.GlobalRole;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CustomUserDetails implements UserDetails {
    Long id;
    String email;
    String password;
    String fullName;
    GlobalRole globalRole;
    Collection<? extends GrantedAuthority> authorities;

    public static CustomUserDetails from(User user){
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getGlobalRole().name());
        return CustomUserDetails.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPasswordHash())
                .fullName(user.getFullName())
                .globalRole(user.getGlobalRole())
                .authorities(Collections.singletonList(authority))
                .build();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
