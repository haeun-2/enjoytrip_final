package com.ssafy.trip.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class EmailPasswordAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;   // email or UserDetails
    private final Object credentials; // password or null

    public EmailPasswordAuthenticationToken(String principal, String credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public EmailPasswordAuthenticationToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities, boolean isBlocked) {
        super(authorities);
        this.principal = userDetails;
        this.credentials = null;
        setAuthenticated(authorities != null && !authorities.isEmpty() && !isBlocked);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
