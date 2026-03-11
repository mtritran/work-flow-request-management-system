package com.mtritran.workflow_request_management_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestHistoryResponse {
    String id;
    String actorUsername;
    String action;
    String note;
    LocalDateTime createdAt;
}
