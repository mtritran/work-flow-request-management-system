package com.mtritran.workflow_request_management_system.controller;

import com.mtritran.workflow_request_management_system.dto.request.PermissionRequest;
import com.mtritran.workflow_request_management_system.dto.response.ApiResponse;
import com.mtritran.workflow_request_management_system.dto.response.PermissionResponse;
import com.mtritran.workflow_request_management_system.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        var result = permissionService.createPermission(request);
        return ApiResponse.<PermissionResponse>builder()
                .code(200)
                .message("Permission created successfully")
                .result(result)
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        var result = permissionService.getAll();
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(200)
                .message("Permissions fetched successfully")
                .result(result)
                .build();
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Void> deletePermission(@PathVariable String name) {
        permissionService.delete(name);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Permission deleted successfully")
                .build();
    }
}
