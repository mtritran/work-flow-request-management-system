package com.mtritran.workflow_request_management_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InspectTokenResponse {
    boolean valid;
    String subject;
    Date expiry;
}
