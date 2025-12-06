package com.mtritran.workflow_request_management_system.mapper;

import com.mtritran.workflow_request_management_system.dto.request.PermissionRequest;
import com.mtritran.workflow_request_management_system.dto.response.PermissionResponse;
import com.mtritran.workflow_request_management_system.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
