package com.candyshop.auth;

import com.candyshop.config.JwtProperties;
import com.candyshop.dto.auth.Token;
import com.candyshop.dto.auth.UserLoginResponse;
import com.candyshop.entity.User;
import com.candyshop.entity.enums.Role;
import com.candyshop.exception.AccessDeniedException;
import com.candyshop.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;
    private final UserService userService;
    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(final Long userId,
                                    final String email,
                                    final Role role) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("id", userId);
        claims.put("role", role);
        Instant validity = Instant.now().plus(jwtProperties.getAccess(), ChronoUnit.HOURS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(final Long userId, final String email) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("id", userId);
        Instant validity = Instant.now()
                .plus(jwtProperties.getRefresh(), ChronoUnit.DAYS);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(key)
                .compact();
    }

    public UserLoginResponse refreshUserTokens(final String refreshToken, final Long userId) {
        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException("Access denied");
        }
        User user = userService.getById(userId);
        Token token = createTokenForResponse(userId, user);

        return getLoginResponse(userId, user, token);

    }

    private static UserLoginResponse getLoginResponse(final Long userId,
                                                      final User user,
                                                      final Token token) {
        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setUserId(userId);
        loginResponse.setName(user.getName());
        loginResponse.setToken(token);
        return loginResponse;
    }

    public boolean validateToken(final String token) {
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(final String token) {
        String email = getEmail(token);
        UserDetails userDetails = userService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(
                userDetails, "",
                userDetails.getAuthorities());
    }

    private Token createTokenForResponse(final Long userId, final User user) {
        Token token = new Token();
        token.setAccessToken(createAccessToken(userId, user.getEmail(), user.getRole()));
        token.setRefreshToken(createRefreshToken(userId, user.getEmail()));
        token.setExpirationIn(jwtProperties.getAccess());
        return token;
    }

    public Token getToken(final User user) {
        Token token = new Token();
        token.setAccessToken(createAccessToken(user.getId(), user.getEmail(), user.getRole()));
        token.setRefreshToken(createRefreshToken(user.getId(), user.getEmail()));
        token.setExpirationIn(jwtProperties.getAccess());
        return token;
    }

    private String getEmail(final String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
