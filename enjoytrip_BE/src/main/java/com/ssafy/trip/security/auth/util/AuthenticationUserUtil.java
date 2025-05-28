package com.ssafy.trip.security.auth.util;

import com.ssafy.trip.security.auth.CustomUserDetails;
import org.springframework.security.core.Authentication;

public class AuthenticationUserUtil {

    public static Long getUserId(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getMember().getId();
        }
        return null;
    }
}
