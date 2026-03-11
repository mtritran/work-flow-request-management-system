package com.mtritran.workflow_request_management_system.dto.request;

import com.mtritran.workflow_request_management_system.enums.RequestType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreationRequest {
    RequestType requestType;

    @Size(min = 10, max = 100, message = "TITLE_INVALID")
    String title;

    String itemName;
    Long price;
    String requestReason;
}




