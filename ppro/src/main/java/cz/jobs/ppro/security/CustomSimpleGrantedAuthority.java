package cz.jobs.ppro.security;

import org.springframework.security.core.GrantedAuthority;

public class CustomSimpleGrantedAuthority implements GrantedAuthority {
    private String role;

    public CustomSimpleGrantedAuthority(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
