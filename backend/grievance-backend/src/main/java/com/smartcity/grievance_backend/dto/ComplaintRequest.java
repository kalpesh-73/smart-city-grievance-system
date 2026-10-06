package com.smartcity.grievance_backend.dto;

import com.smartcity.grievance_backend.entity.Category;
import com.smartcity.grievance_backend.entity.Priority;
import lombok.Data;

@Data
public class ComplaintRequest {
    private String title;
    private String description;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String address;
    private String ward;
    private Category category;
    private Priority priority;
    private Long citizenId;
}