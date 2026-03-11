package com.mtritran.workflow_request_management_system.service;

import com.mtritran.workflow_request_management_system.dto.request.RequestCreationRequest;
import com.mtritran.workflow_request_management_system.dto.response.RequestResponse;
import com.mtritran.workflow_request_management_system.entity.Request;
import com.mtritran.workflow_request_management_system.entity.User;
import com.mtritran.workflow_request_management_system.enums.RequestStatus;
import com.mtritran.workflow_request_management_system.exception.AppException;
import com.mtritran.workflow_request_management_system.exception.ErrorCode;
import com.mtritran.workflow_request_management_system.repository.RequestRepository;
import com.mtritran.workflow_request_management_system.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestService {
    RequestRepository requestRepository;
    UserRepository userRepository;

    @Transactional
    public RequestResponse createRequest(RequestCreationRequest request) {
        User user = getCurrentUser();

        Request newRequest = Request.builder()
                .requestType(request.getRequestType())
                .title(request.getTitle())
                .itemName(request.getItemName())
                .price(request.getPrice())
                .requestReason(request.getRequestReason())
                .status(RequestStatus.PENDING)
                .requestedBy(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return mapToRequestResponse(requestRepository.save(newRequest));
    }

    public List<RequestResponse> getMyRequests() {
        User user = getCurrentUser();
        return requestRepository.findByRequestedBy(user).stream()
                .map(this::mapToRequestResponse)
                .toList();
    }

    public List<RequestResponse> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(this::mapToRequestResponse)
                .toList();
    }

    public RequestResponse getRequestById(String requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        return mapToRequestResponse(request);
    }

    @Transactional
    public RequestResponse processRequest(String requestId, RequestStatus newStatus, String note) {
        if (newStatus == RequestStatus.PENDING) {
            throw new IllegalArgumentException("Invalid status for processing");
        }

        User processor = getCurrentUser();
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AppException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        request.setStatus(newStatus);
        request.setProcessedBy(processor);
        request.setProcessedNote(note);
        request.setUpdatedAt(LocalDateTime.now());

        return mapToRequestResponse(requestRepository.save(request));
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private RequestResponse mapToRequestResponse(Request request) {
        return RequestResponse.builder()
                .id(request.getId())
                .requestType(request.getRequestType())
                .title(request.getTitle())
                .status(request.getStatus())
                .requestedBy(request.getRequestedBy().getUsername())
                .processedBy(request.getProcessedBy() != null ? request.getProcessedBy().getUsername() : null)
                .itemName(request.getItemName())
                .price(request.getPrice())
                .requestReason(request.getRequestReason())
                .processedNote(request.getProcessedNote())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
    }
}

