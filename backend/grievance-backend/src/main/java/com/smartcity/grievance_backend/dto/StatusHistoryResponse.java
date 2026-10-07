package com.smartcity.grievance_backend.dto;

import com.smartcity.grievance_backend.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatusHistoryResponse {
    private Long id;
    private Status status;
    private String remarks;
    private Long updatedById;
    private String updatedByName;
    private LocalDateTime createdAt;
}