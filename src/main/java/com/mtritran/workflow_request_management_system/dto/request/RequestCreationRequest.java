package com.mtritran.workflow_request_management_system.dto.request;

import com.mtritran.workflow_request_management_system.enums.RequestType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreationRequest {
    RequestType requestType;
    String title;
    String description;
    Map<String, String> details;
}




