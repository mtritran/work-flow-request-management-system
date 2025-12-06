package com.mtritran.workflow_request_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "request_details")
public class RequestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    Request request;

    @Column(nullable = false)
    String fieldName;

    @Column(columnDefinition = "TEXT")
    String fieldValue;
}




