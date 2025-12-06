package com.mtritran.workflow_request_management_system.entity;

import com.mtritran.workflow_request_management_system.enums.RequestStatus;
import com.mtritran.workflow_request_management_system.enums.RequestType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

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

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    RequestStatus status;

    @ManyToOne
    @JoinColumn(name = "requested_by", nullable = false)
    User requestedBy;

    @ManyToOne
    @JoinColumn(name = "approved_by", nullable = true)
    User approvedBy;

    @ManyToOne
    @JoinColumn(name = "rejected_by", nullable = true)
    User rejectedBy;

    @Column(columnDefinition = "TEXT")
    String rejectionReason;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RequestDetail> details;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime approvedAt;
    LocalDateTime rejectedAt;
}




