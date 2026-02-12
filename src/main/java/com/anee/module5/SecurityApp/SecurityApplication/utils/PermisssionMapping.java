package com.anee.module5.SecurityApp.SecurityApplication.utils;

import com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Permissions;
import com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Permissions.*;
import static com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Role.*;

public class PermisssionMapping {

    private static final Map<Role, Set<Permissions>> map = Map.of(
            USER, Set.of(USER_VIEW, POST_VIEW),
            CREATOR, Set.of(POST_CREATE, USER_UPDATE, POST_UPDATE),
            ADMIN, Set.of(USER_DELETE, USER_CREATE, POST_DELETE, POST_CREATE, USER_UPDATE, POST_UPDATE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role) {
        return map.get(role).stream()
                .map(permissions -> new SimpleGrantedAuthority(permissions.name()))
                .collect(Collectors.toSet());
    }
}
