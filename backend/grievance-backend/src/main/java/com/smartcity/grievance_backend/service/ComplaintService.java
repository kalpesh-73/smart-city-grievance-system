package com.smartcity.grievance_backend.service;
import org.springframework.transaction.annotation.Transactional;
import com.smartcity.grievance_backend.dto.ComplaintRequest;
import com.smartcity.grievance_backend.dto.ComplaintResponse;
import com.smartcity.grievance_backend.entity.Complaint;
import com.smartcity.grievance_backend.entity.Priority;
import com.smartcity.grievance_backend.entity.User;
import com.smartcity.grievance_backend.repository.ComplaintRepository;
import com.smartcity.grievance_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    public ComplaintResponse createComplaint(ComplaintRequest req) {
        User citizen = userRepository.findById(req.getCitizenId())
                .orElseThrow(() -> new RuntimeException("Citizen not found"));

        Complaint complaint = new Complaint();
        complaint.setTitle(req.getTitle());
        complaint.setDescription(req.getDescription());
        complaint.setImageUrl(req.getImageUrl());
        complaint.setLatitude(req.getLatitude());
        complaint.setLongitude(req.getLongitude());
        complaint.setAddress(req.getAddress());
        complaint.setWard(req.getWard());
        complaint.setCategory(req.getCategory() != null ? req.getCategory() : com.smartcity.grievance_backend.entity.Category.OTHER);
        complaint.setPriority(req.getPriority() != null ? req.getPriority() : Priority.MEDIUM);
        complaint.setCitizen(citizen);

        // Set SLA deadline based on priority
        complaint.setSlaDeadline(calculateSla(complaint.getPriority()));

        // Simple rule-based department routing
        complaint.setDepartment(routeDepartment(complaint.getCategory()));

        Complaint saved = complaintRepository.save(complaint);
        return toResponse(saved);
    }

    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ComplaintResponse getComplaintById(Long id) {
        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        return toResponse(c);
    }

    public List<ComplaintResponse> getComplaintsByCitizen(Long citizenId) {
        return complaintRepository.findByCitizenId(citizenId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getComplaintsByDepartment(String department) {
        return complaintRepository.findByDepartment(department)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ComplaintResponse> getComplaintsByWorker(Long workerId) {
        return complaintRepository.findByAssignedWorkerId(workerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ComplaintResponse updateStatus(Long id, String status, Long workerId) {
        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        c.setStatus(com.smartcity.grievance_backend.entity.Status.valueOf(status.toUpperCase()));

        if (workerId != null) {
            User worker = userRepository.findById(workerId)
                    .orElseThrow(() -> new RuntimeException("Worker not found"));
            c.setAssignedWorker(worker);
        }

        Complaint saved = complaintRepository.save(c);
        return toResponse(saved);
    }

    // ---- helpers ----

    private LocalDateTime calculateSla(Priority priority) {
        LocalDateTime now = LocalDateTime.now();
        return switch (priority) {
            case URGENT -> now.plusHours(6);
            case HIGH -> now.plusHours(24);
            case MEDIUM -> now.plusDays(3);
            case LOW -> now.plusDays(7);
        };
    }

    private String routeDepartment(com.smartcity.grievance_backend.entity.Category category) {
        return switch (category) {
            case ROADS, WATERLOGGING -> "Roads";
            case GARBAGE -> "Sanitation";
            case STREETLIGHT, ELECTRICITY -> "Electricity";
            case WATER_SUPPLY -> "Water";
            case DRAINAGE -> "Drainage";
            case ILLEGAL_CONSTRUCTION, PUBLIC_PROPERTY -> "Public Works";
            default -> "General";
        };
    }

    private ComplaintResponse toResponse(Complaint c) {
        ComplaintResponse r = new ComplaintResponse();
        r.setId(c.getId());
        r.setTitle(c.getTitle());
        r.setDescription(c.getDescription());
        r.setImageUrl(c.getImageUrl());
        r.setLatitude(c.getLatitude());
        r.setLongitude(c.getLongitude());
        r.setAddress(c.getAddress());
        r.setWard(c.getWard());
        r.setCategory(c.getCategory());
        r.setPriority(c.getPriority());
        r.setStatus(c.getStatus());
        r.setDepartment(c.getDepartment());
        r.setAiConfidence(c.getAiConfidence());
        r.setCreatedAt(c.getCreatedAt());
        r.setUpdatedAt(c.getUpdatedAt());
        r.setSlaDeadline(c.getSlaDeadline());

        if (c.getCitizen() != null) {
            r.setCitizenId(c.getCitizen().getId());
            r.setCitizenName(c.getCitizen().getName());
        }
        if (c.getAssignedWorker() != null) {
            r.setAssignedWorkerId(c.getAssignedWorker().getId());
        }
        return r;
    }
}