package com.mtritran.workflow_request_management_system.repository;

import com.mtritran.workflow_request_management_system.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    void deleteByName(String name);
    Optional<Permission> findByName(String name);
    boolean existsByName(String name);
    List<Permission> findByNameIn(Set<String> names);
}
