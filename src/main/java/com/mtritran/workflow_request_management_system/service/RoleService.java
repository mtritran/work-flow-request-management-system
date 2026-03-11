package com.mtritran.workflow_request_management_system.service;

import com.mtritran.workflow_request_management_system.dto.request.RoleRequest;
import com.mtritran.workflow_request_management_system.dto.response.RoleResponse;
import com.mtritran.workflow_request_management_system.exception.AppException;
import com.mtritran.workflow_request_management_system.exception.ErrorCode;
import com.mtritran.workflow_request_management_system.mapper.RoleMapper;
import com.mtritran.workflow_request_management_system.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        var role = roleMapper.toRole(request);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public RoleResponse getRole(String name) {
        var role = roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        return roleMapper.toRoleResponse(role);
    }

    public RoleResponse updateRole(String name, RoleRequest request) {
        var role = roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        if (request.getDescription() != null) {
            role.setDescription(request.getDescription());
        }

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public void deleteRole(String name) {
        var role = roleRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roleRepository.delete(role);
    }
}