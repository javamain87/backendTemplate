package com.template.exam.biz.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.template.exam.biz.auth.model.UserVO;
import com.template.exam.biz.common.exception.InfraException;
import com.template.exam.biz.common.jwt.dto.JwtResponseDTO;
import com.template.exam.biz.common.exception.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Algorithm algorithm;

    private final long accessTokenMilliseconds;
    private final long refreshTokenMilliseconds;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.expiration-time}") long accessKeyValidationTime,
        @Value("${jwt.refresh-token-expiration-time}") long refreshKeyValidationTime
    ) {
        this.algorithm = Algorithm.HMAC512(secretKey);
        this.accessTokenMilliseconds = accessKeyValidationTime * 1000;
        this.refreshTokenMilliseconds = refreshKeyValidationTime * 1000;
    }
    public JwtResponseDTO createToken(UserVO authEntity, Collection<? extends GrantedAuthority> authorities) {
        try {
            Date now = new Date();
            Date accessTokenValidity = new Date(now.getTime() + accessTokenMilliseconds);
            Date refreshTokenValidity = new Date(now.getTime() + refreshTokenMilliseconds);

            String accessToken = JWT.create()
                    .withSubject(authEntity.getUserId())
                    .withClaim("userId", authEntity.getUserId())
                    .withClaim("userName", authEntity.getUserName())
                    .withClaim("auth", authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .withSubject(authEntity.getEmail())
                    .withIssuedAt(now)
                    .withExpiresAt(accessTokenValidity)
                    .sign(algorithm);

            String refreshToken = JWT.create()
                    .withSubject(authEntity.getUserId())
                    .withIssuedAt(now)
                    .withExpiresAt(refreshTokenValidity)
                    .sign(algorithm);

            return JwtResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch(Exception e) {
            throw new InfraException(JwtException.UNDEFINED_EXCEPTION.getCode(), JwtException.UNDEFINED_EXCEPTION.getMessage(), e);
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(refreshToken);
            return true;
        } catch (Exception e) {
            throw new InfraException(JwtException.UNDEFINED_EXCEPTION.getCode(), JwtException.UNDEFINED_EXCEPTION.getMessage(), e);
        }
    }

    public String getUserIdFromToken(String refreshToken) {
        try {
            DecodedJWT decodedJWT = JWT.decode(refreshToken);
            return decodedJWT.getSubject();
        } catch (Exception e) {
            throw new InfraException(JwtException.INVALID_CLAIM_EXCEPTION.getCode(), JwtException.INVALID_CLAIM_EXCEPTION.getMessage(), e);
        }
    }

    public String resolveToken(HttpServletRequest request) {
        try {
            String bearerToken = request.getHeader("Authorization");
            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
            return null;
        } catch (Exception e) {
            throw new InfraException(JwtException.INVALID_CLAIM_EXCEPTION.getCode(), JwtException.INVALID_CLAIM_EXCEPTION.getMessage(), e);
        }
    }

    public int validateToken(String token) {
        log.info("******** validateToken: {}", token);
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return 1; // 검사 성공 시 1 반환
        } catch (TokenExpiredException e) {
            log.error("Token validation failed: {}", e.getMessage(), e);
            throw new InfraException(JwtException.TOKEN_EXPIRED_EXCEPTION.getCode(), JwtException.TOKEN_EXPIRED_EXCEPTION.getMessage());
        } catch (SignatureVerificationException e) {
            log.error("Token validation failed: {}", e.getMessage(), e);
            throw new InfraException(JwtException.SIGNATURE_VERIFICATION_EXCEPTION.getCode(), JwtException.SIGNATURE_VERIFICATION_EXCEPTION.getMessage());
        } catch (AlgorithmMismatchException e) {
            log.error("AlgorithmMismatchException: {}", e.getMessage(), e);
            throw new InfraException(JwtException.ALGORITHM_MISMATCH_EXCEPTION.getCode(), JwtException.ALGORITHM_MISMATCH_EXCEPTION.getMessage());
        } catch (InvalidClaimException e) {
            log.error("InvalidClaimException: {}", e.getMessage(), e);
            throw new InfraException(JwtException.INVALID_CLAIM_EXCEPTION.getCode(), JwtException.INVALID_CLAIM_EXCEPTION.getMessage());
        } catch (Exception e) {
            log.error("Undefined Error: {}", e.getMessage(), e);
            throw new InfraException(JwtException.UNDEFINED_EXCEPTION.getCode(), JwtException.UNDEFINED_EXCEPTION.getMessage());
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String username = decodedJWT.getSubject();
            String[] authStrings = decodedJWT.getClaim("auth").asArray(String.class);
            Collection<? extends GrantedAuthority> authorities = Arrays.stream(authStrings)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetails userDetails = new User(username, "", authorities);

            return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        } catch (Exception e) {
            throw new InfraException(JwtException.INVALID_CLAIM_EXCEPTION.getCode(), JwtException.INVALID_CLAIM_EXCEPTION.getMessage(), e);
        }
    }


}
