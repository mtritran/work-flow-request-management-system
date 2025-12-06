package com.mtritran.workflow_request_management_system.mapper;

import com.mtritran.workflow_request_management_system.dto.request.RoleRequest;
import com.mtritran.workflow_request_management_system.dto.response.RoleResponse;
import com.mtritran.workflow_request_management_system.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
