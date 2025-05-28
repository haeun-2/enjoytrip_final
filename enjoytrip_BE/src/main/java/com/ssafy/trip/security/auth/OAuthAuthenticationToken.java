package com.ssafy.trip.security.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class OAuthAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;   // email or UserDetails
    private final Object credentials; // oauth provider key or null

    public OAuthAuthenticationToken(String providerId, String principal) {
        super(null);
        this.principal = principal;
        this.credentials = providerId;
        setAuthenticated(false);
    }

    public OAuthAuthenticationToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities, boolean isBlocked) {
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
