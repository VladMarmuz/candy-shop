package com.candyshop.auth;

import com.candyshop.entity.User;
import com.candyshop.entity.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtEntityFactory {

    public static JwtEntity create(final User user) {
        return new JwtEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                mapToGrantedAuthorities(new ArrayList<>(
                        user.getRole()
                                .ordinal()))
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(
            final List<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
