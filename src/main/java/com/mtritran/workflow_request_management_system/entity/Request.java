package com.mtritran.workflow_request_management_system.entity;

import com.mtritran.workflow_request_management_system.enums.RequestStatus;
import com.mtritran.workflow_request_management_system.enums.RequestType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RequestType requestType;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String itemName; // Name of equipment or document

    Long price; // Only for equipment

    @Column(columnDefinition = "TEXT")
    String requestReason;

    @Column(columnDefinition = "TEXT")
    String processedNote; // Admin's note during approve/reject

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "requested_by", nullable = false)
    User requestedBy;

    @ManyToOne
    @JoinColumn(name = "processed_by")
    User processedBy;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}




