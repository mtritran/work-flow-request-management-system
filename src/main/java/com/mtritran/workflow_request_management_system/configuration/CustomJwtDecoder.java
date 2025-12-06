package com.mtritran.workflow_request_management_system.configuration;

import com.mtritran.workflow_request_management_system.repository.InvalidatedTokenRepository;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Primary
public class CustomJwtDecoder implements JwtDecoder {

    private final @Qualifier("nimbusJwtDecoder") JwtDecoder delegate;
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Override
    public Jwt decode(String token) throws JwtException {
        Jwt jwt = delegate.decode(token);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();

            if (invalidatedTokenRepository.existsById(jti)) {
                throw new JwtException("Token has been revoked");
            }
        } catch (ParseException e) {
            throw new JwtException("Invalid token", e);
        }

        return jwt;
    }
}
