package com.mtritran.workflow_request_management_system.repository;

import com.mtritran.workflow_request_management_system.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
    int deleteByExpiryTimeBefore(LocalDateTime time);
}
