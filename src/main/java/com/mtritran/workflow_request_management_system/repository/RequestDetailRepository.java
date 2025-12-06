package com.mtritran.workflow_request_management_system.repository;


import com.mtritran.workflow_request_management_system.entity.Request;
import com.mtritran.workflow_request_management_system.entity.RequestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestDetailRepository extends JpaRepository<RequestDetail, String> {
    List<RequestDetail> findByRequest(Request request);
}

