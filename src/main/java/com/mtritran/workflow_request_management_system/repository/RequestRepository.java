package com.mtritran.workflow_request_management_system.repository;


import com.mtritran.workflow_request_management_system.entity.Request;
import com.mtritran.workflow_request_management_system.entity.User;
import com.mtritran.workflow_request_management_system.enums.RequestStatus;
import com.mtritran.workflow_request_management_system.enums.RequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, String> {
    List<Request> findByRequestedBy(User user);
    
    List<Request> findByStatus(RequestStatus status);
    
    List<Request> findByRequestType(RequestType requestType);
    
    List<Request> findByRequestedByAndStatus(User user, RequestStatus status);
}

