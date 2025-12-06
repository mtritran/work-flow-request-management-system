package com.mtritran.workflow_request_management_system.dto.request;

import com.mtritran.workflow_request_management_system.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "USER_NAME_INVALID")
    String username;
    @Size(min = 7, message = "PASSWORD_INVALID")
    String password;
    String firstname;
    String lastname;
    @DobConstraint(min = 18, message = "DOB_INVALID")
    LocalDate dob;
}

