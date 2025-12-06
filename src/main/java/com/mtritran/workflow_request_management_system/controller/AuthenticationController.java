package com.mtritran.workflow_request_management_system.controller;

import com.mtritran.workflow_request_management_system.dto.request.AuthenticationRequest;
import com.mtritran.workflow_request_management_system.dto.request.InspectTokenRequest;
import com.mtritran.workflow_request_management_system.dto.request.LogoutRequest;
import com.mtritran.workflow_request_management_system.dto.response.ApiResponse;
import com.mtritran.workflow_request_management_system.dto.response.AuthenticationResponse;
import com.mtritran.workflow_request_management_system.dto.response.InspectTokenResponse;
import com.mtritran.workflow_request_management_system.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authResponse = authenticationService.authenticate(request);

        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Success")
                .result(authResponse)
                .build();
    }

    @PostMapping("/inspect")
    ApiResponse<InspectTokenResponse> inspectToken(@RequestBody InspectTokenRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.inspectToken(request);

        return ApiResponse.<InspectTokenResponse>builder()
                .code(200)
                .message("Success")
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Logout successful")
                .result("Token has been invalidated")
                .build();
    }
}
