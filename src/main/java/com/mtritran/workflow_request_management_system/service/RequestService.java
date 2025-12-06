package com.mtritran.workflow_request_management_system.service;

import com.mtritran.workflow_request_management_system.dto.request.RequestCreationRequest;
import com.mtritran.workflow_request_management_system.dto.response.RequestResponse;
import com.mtritran.workflow_request_management_system.entity.Request;
import com.mtritran.workflow_request_management_system.entity.RequestDetail;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RequestService {
    RequestRepository requestRepository;
    UserRepository userRepository;

    @Transactional
    public RequestResponse createRequest(RequestCreationRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Request newRequest = Request.builder()
                .requestType(request.getRequestType())
                .title(request.getTitle())
                .description(request.getDescription())
                .status(RequestStatus.PENDING)
                .requestedBy(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .details(new HashSet<>())
                .build();

        // Add request details if provided
        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            Set<RequestDetail> details = request.getDetails().entrySet().stream()
                    .map(entry -> RequestDetail.builder()
                            .request(newRequest)
                            .fieldName(entry.getKey())
                            .fieldValue(entry.getValue())
                            .build())
                    .collect(Collectors.toSet());
            newRequest.setDetails(details);
        }

        Request savedRequest = requestRepository.save(newRequest);
        return mapToRequestResponse(savedRequest);
    }

    public List<RequestResponse> getMyRequests() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Request> requests = requestRepository.findByRequestedBy(user);
        return requests.stream()
                .map(this::mapToRequestResponse)
                .toList();
    }

    public List<RequestResponse> getAllRequests() {
        List<Request> requests = requestRepository.findAll();
        return requests.stream()
                .map(this::mapToRequestResponse)
                .toList();
    }

    public RequestResponse getRequestById(String requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));
        return mapToRequestResponse(request);
    }

    @Transactional
    public RequestResponse approveRequest(String requestId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User approver = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AppException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        request.setStatus(RequestStatus.APPROVED);
        request.setApprovedBy(approver);
        request.setApprovedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        Request savedRequest = requestRepository.save(request);
        return mapToRequestResponse(savedRequest);
    }

    @Transactional
    public RequestResponse rejectRequest(String requestId, String reason) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User rejector = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AppException(ErrorCode.REQUEST_ALREADY_PROCESSED);
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectedBy(rejector);
        request.setRejectionReason(reason);
        request.setRejectedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());

        Request savedRequest = requestRepository.save(request);
        return mapToRequestResponse(savedRequest);
    }

    private RequestResponse mapToRequestResponse(Request request) {
        Map<String, String> detailsMap = request.getDetails() != null
                ? request.getDetails().stream()
                .collect(Collectors.toMap(
                        RequestDetail::getFieldName,
                        RequestDetail::getFieldValue
                ))
                : Map.of();

        return RequestResponse.builder()
                .id(request.getId())
                .requestType(request.getRequestType())
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .requestedBy(request.getRequestedBy().getUsername())
                .approvedBy(request.getApprovedBy() != null ? request.getApprovedBy().getUsername() : null)
                .rejectedBy(request.getRejectedBy() != null ? request.getRejectedBy().getUsername() : null)
                .rejectionReason(request.getRejectionReason())
                .details(detailsMap)
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .approvedAt(request.getApprovedAt())
                .rejectedAt(request.getRejectedAt())
                .build();
    }
}

