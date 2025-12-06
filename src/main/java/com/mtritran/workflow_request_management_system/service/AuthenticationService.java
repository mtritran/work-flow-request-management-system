package com.mtritran.workflow_request_management_system.service;

import com.mtritran.workflow_request_management_system.dto.request.AuthenticationRequest;
import com.mtritran.workflow_request_management_system.dto.request.InspectTokenRequest;
import com.mtritran.workflow_request_management_system.dto.request.LogoutRequest;
import com.mtritran.workflow_request_management_system.dto.response.AuthenticationResponse;
import com.mtritran.workflow_request_management_system.dto.response.InspectTokenResponse;
import com.mtritran.workflow_request_management_system.exception.AppException;
import com.mtritran.workflow_request_management_system.exception.ErrorCode;
import com.mtritran.workflow_request_management_system.entity.User;
import com.mtritran.workflow_request_management_system.entity.InvalidatedToken;
import com.mtritran.workflow_request_management_system.repository.InvalidatedTokenRepository;
import com.mtritran.workflow_request_management_system.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.signerKey}")
    @NonFinal
    protected String SIGNER_KEY;

    // LOGIN
    public AuthenticationResponse authenticate(AuthenticationRequest req) {
        var user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .authenticated(true)
                .message("Login successful")
                .token(token)
                .build();
    }

    // INSPECT (không fail – chỉ báo valid)
    public InspectTokenResponse inspectToken(InspectTokenRequest request)
            throws JOSEException, ParseException {

        SignedJWT signed = SignedJWT.parse(request.getToken());

        // Verify signature (để tránh đọc data từ token giả mạo)
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        boolean sigOk = signed.verify(verifier);

        Date exp = signed.getJWTClaimsSet().getExpirationTime();
        String sub = signed.getJWTClaimsSet().getSubject();
        String jti = signed.getJWTClaimsSet().getJWTID();

        boolean notExpired = exp != null && exp.after(new Date());
        boolean notRevoked = (jti != null) && !invalidatedTokenRepository.existsById(jti);

        boolean valid = sigOk && notExpired && notRevoked;

        return InspectTokenResponse.builder()
                .valid(valid)
                .subject(sub)
                .expiry(exp)
                .build();
    }

    // LOGOUT (idempotent)
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signed = SignedJWT.parse(request.getToken());

        // Chỉ cần verify signature + còn hạn (để không lưu rác)
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        if (!signed.verify(verifier)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        Date exp = signed.getJWTClaimsSet().getExpirationTime();
        if (exp == null || exp.before(new Date())) {
            // token hết hạn thì coi như đã “vô hiệu” tự nhiên → không cần lưu
            return;
        }

        String jti = signed.getJWTClaimsSet().getJWTID();
        if (jti == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (!invalidatedTokenRepository.existsById(jti)) {
            // Convert Date to LocalDateTime
            LocalDateTime expiryTime = exp.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            invalidatedTokenRepository.save(
                    InvalidatedToken.builder().id(jti).expiryTime(expiryTime).build()
            );
        }
    }

    // Helper: phát hành token
    private String generateToken(User user) {
        var header = new JWSHeader(JWSAlgorithm.HS512);
        var claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("mtritran.com")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user)) // "ADMIN USER"
                .build();

        var jws = new JWSObject(header, new Payload(claims.toJSONObject()));
        try {
            jws.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jws.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        var joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(r -> joiner.add(r.getName())); // => "ADMIN USER"
        }
        return joiner.toString();
    }
}
