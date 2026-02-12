package com.anee.module5.SecurityApp.SecurityApplication.dto;

import com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Permissions;
import com.anee.module5.SecurityApp.SecurityApplication.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {

    private String email;
    private String password;
    private String name;
    private Set<Role> roles;
    private Set<Permissions> permissions;
}
