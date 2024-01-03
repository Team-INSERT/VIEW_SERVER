package com.insert.view.infra.jwt.auth;

import com.insert.view.infra.error.exception.ErrorCode;
import com.insert.view.infra.error.exception.ViewException;
import com.insert.view.infra.jwt.properties.JwtConstants;
import com.insert.view.infra.jwt.util.JwtUtil;
import com.insert.view.infra.security.auth.AuthDetailsService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuth {
    private final JwtUtil jwtUtil;
    private final AuthDetailsService authDetailsService;

    public Authentication authentication(String token) {
        Claims claims = jwtUtil.getJwt(token).getBody();

        if (isNotAccessToken(token)) {
            throw new ViewException(ErrorCode.INVALID_TOKEN);
        }

        UserDetails userDetails = authDetailsService.loadUserByUsername(claims.get(JwtConstants.AUTH_ID.message).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private boolean isNotAccessToken(String token) {
        if (token.isEmpty()) {
            throw new ViewException(ErrorCode.INVALID_TOKEN);
        }
        String role = jwtUtil.getJwt(token).getHeader().get(JwtConstants.TYPE.message).toString();
        return !role.equals(JwtConstants.ACCESS_KEY.message);
    }
}
