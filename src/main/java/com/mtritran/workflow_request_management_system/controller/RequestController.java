package com.mtritran.workflow_request_management_system.controller;

import com.mtritran.workflow_request_management_system.dto.request.RequestApprovalRequest;
import com.mtritran.workflow_request_management_system.dto.request.RequestCreationRequest;
import com.mtritran.workflow_request_management_system.dto.response.ApiResponse;
import com.mtritran.workflow_request_management_system.dto.response.RequestResponse;
import com.mtritran.workflow_request_management_system.service.RequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestController {
    RequestService requestService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<RequestResponse> createRequest(@RequestBody RequestCreationRequest request) {
        return ApiResponse.<RequestResponse>builder()
                .code(200)
                .message("Request created successfully")
                .result(requestService.createRequest(request))
                .build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<List<RequestResponse>> getMyRequests() {
        return ApiResponse.<List<RequestResponse>>builder()
                .code(200)
                .message("Requests retrieved successfully")
                .result(requestService.getMyRequests())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<RequestResponse>> getAllRequests() {
        return ApiResponse.<List<RequestResponse>>builder()
                .code(200)
                .message("All requests retrieved successfully")
                .result(requestService.getAllRequests())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ApiResponse<RequestResponse> getRequestById(@PathVariable String id) {
        return ApiResponse.<RequestResponse>builder()
                .code(200)
                .message("Request retrieved successfully")
                .result(requestService.getRequestById(id))
                .build();
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RequestResponse> approveRequest(@PathVariable String id) {
        return ApiResponse.<RequestResponse>builder()
                .code(200)
                .message("Request approved successfully")
                .result(requestService.approveRequest(id))
                .build();
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<RequestResponse> rejectRequest(
            @PathVariable String id,
            @RequestBody RequestApprovalRequest request) {
        return ApiResponse.<RequestResponse>builder()
                .code(200)
                .message("Request rejected successfully")
                .result(requestService.rejectRequest(id, request.getReason()))
                .build();
    }
}




