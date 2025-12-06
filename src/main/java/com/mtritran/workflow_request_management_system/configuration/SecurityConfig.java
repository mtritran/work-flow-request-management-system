package com.mtritran.workflow_request_management_system.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtritran.workflow_request_management_system.dto.response.ApiResponse;
import com.mtritran.workflow_request_management_system.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    private static final String[] SWAGGER_ENDPOINTS = {
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtDecoder jwtDecoder) throws Exception {
        http.authorizeHttpRequests(request ->
                request
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((req, res, ex) -> {
                    ErrorCode error = ErrorCode.UNAUTHENTICATED;
                    ApiResponse<?> body = ApiResponse.builder()
                            .code(error.getCode())
                            .message(error.getMessage())
                            .build();
                    res.setStatus(error.getStatusCode().value());
                    res.setContentType("application/json");
                    res.getWriter().write(objectMapper.writeValueAsString(body));
                })
                .accessDeniedHandler((req, res, ex) -> {
                    ErrorCode error = ErrorCode.UNAUTHORIZED;
                    ApiResponse<?> body = ApiResponse.builder()
                            .code(error.getCode())
                            .message(error.getMessage())
                            .build();
                    res.setStatus(error.getStatusCode().value());
                    res.setContentType("application/json");
                    res.getWriter().write(objectMapper.writeValueAsString(body));
                })
        );

        // Spring sẽ inject CustomJwtDecoder ở đây (vì nó implement JwtDecoder)
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
        );

        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // Nimbus decoder (delegate cho CustomJwtDecoder)
    @Bean("nimbusJwtDecoder")
    JwtDecoder nimbusJwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("scope");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
