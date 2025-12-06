package com.mtritran.workflow_request_management_system.configuration;

import com.mtritran.workflow_request_management_system.entity.Role;
import com.mtritran.workflow_request_management_system.entity.User;
import com.mtritran.workflow_request_management_system.repository.RoleRepository;
import com.mtritran.workflow_request_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            // Tạo role USER
            roleRepository.findByName("USER").orElseGet(() -> {
                Role userRole = Role.builder().name("USER").build();
                return roleRepository.save(userRole);
            });

            // Tạo role ADMIN
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                Role role = Role.builder().name("ADMIN").build();
                return roleRepository.save(role);
            });

            // Tạo tài khoản admin mặc định
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(Set.of(adminRole))
                        .build();
                userRepository.save(admin);
                log.warn("Admin account created: admin/admin");
            }
        };
    }
}
