package com.smartcity.grievance_backend.dto;

import com.smartcity.grievance_backend.entity.Category;
import com.smartcity.grievance_backend.entity.Priority;
import com.smartcity.grievance_backend.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComplaintResponse {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private String ward;
    private Category category;
    private Priority priority;
    private Status status;
    private Long citizenId;
    private String citizenName;
    private String department;
    private Long assignedWorkerId;
    private Double aiConfidence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime slaDeadline;
}