package com.mtritran.workflow_request_management_system.service;

import com.mtritran.workflow_request_management_system.dto.request.UserCreationRequest;
import com.mtritran.workflow_request_management_system.dto.request.UserUpdateRequest;
import com.mtritran.workflow_request_management_system.dto.response.UserResponse;
import com.mtritran.workflow_request_management_system.entity.Role;
import com.mtritran.workflow_request_management_system.entity.User;
import com.mtritran.workflow_request_management_system.enums.RoleEnum;
import com.mtritran.workflow_request_management_system.exception.AppException;
import com.mtritran.workflow_request_management_system.exception.ErrorCode;
import com.mtritran.workflow_request_management_system.mapper.UserMapper;
import com.mtritran.workflow_request_management_system.repository.RoleRepository;
import com.mtritran.workflow_request_management_system.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName(RoleEnum.USER.name())
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_ROLE_NOT_FOUND));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .toList();
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.UNDEFINED_EXCEPTION));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            var roles = roleRepository.findByNameIn(request.getRoles());
            if (roles.isEmpty()) {
                throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
            }
            user.setRoles(new HashSet<>(roles));
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateMyInfo(UserUpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // user thường không được đổi role, nên mình không set roles ở đây
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new AppException(ErrorCode.UNDEFINED_EXCEPTION);
        }
        userRepository.deleteById(id);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(
                ()->new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }
}

