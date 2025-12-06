package com.mtritran.workflow_request_management_system.mapper;

import com.mtritran.workflow_request_management_system.dto.request.UserCreationRequest;
import com.mtritran.workflow_request_management_system.dto.request.UserUpdateRequest;
import com.mtritran.workflow_request_management_system.dto.response.UserResponse;
import com.mtritran.workflow_request_management_system.entity.Role;
import com.mtritran.workflow_request_management_system.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);

    default Set<String> mapRoles(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}

