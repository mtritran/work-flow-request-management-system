package com.mtritran.workflow_request_management_system.dto.response;

import com.mtritran.workflow_request_management_system.enums.RequestStatus;
import com.mtritran.workflow_request_management_system.enums.RequestType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestResponse {
    String id;
    RequestType requestType;
    String title;
    String description;
    RequestStatus status;
    String requestedBy;
    String processedBy;
    String itemName;
    Long price;
    String requestReason;
    String processedNote;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

