package com.smartcity.grievance_backend.service;

import com.smartcity.grievance_backend.dto.ComplaintRequest;
import com.smartcity.grievance_backend.dto.ComplaintResponse;
import com.smartcity.grievance_backend.dto.StatusHistoryResponse;
import com.smartcity.grievance_backend.entity.Complaint;
import com.smartcity.grievance_backend.entity.ComplaintStatusHistory;
import com.smartcity.grievance_backend.entity.Priority;
import com.smartcity.grievance_backend.entity.Status;
import com.smartcity.grievance_backend.entity.User;
import com.smartcity.grievance_backend.repository.ComplaintRepository;
import com.smartcity.grievance_backend.repository.ComplaintStatusHistoryRepository;
import com.smartcity.grievance_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ComplaintStatusHistoryRepository historyRepository;

    // ---------------- CREATE ----------------

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
        complaint.setCategory(req.getCategory() != null
                ? req.getCategory()
                : com.smartcity.grievance_backend.entity.Category.OTHER);
        complaint.setPriority(req.getPriority() != null ? req.getPriority() : Priority.MEDIUM);
        complaint.setCitizen(citizen);
        complaint.setSlaDeadline(calculateSla(complaint.getPriority()));
        complaint.setDepartment(routeDepartment(complaint.getCategory()));

        Complaint saved = complaintRepository.save(complaint);

        // Log initial status
        ComplaintStatusHistory history = new ComplaintStatusHistory();
        history.setComplaint(saved);
        history.setStatus(saved.getStatus());
        history.setRemarks("Complaint submitted by citizen");
        history.setUpdatedBy(citizen);
        historyRepository.save(history);

        return toResponse(saved);
    }

    // ---------------- READ ----------------

    @Transactional(readOnly = true)
    public List<ComplaintResponse> getAllComplaints() {
        return complaintRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ComplaintResponse getComplaintById(Long id) {
        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        return toResponse(c);
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponse> getComplaintsByCitizen(Long citizenId) {
        return complaintRepository.findByCitizenId(citizenId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponse> getComplaintsByDepartment(String department) {
        return complaintRepository.findByDepartment(department)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ComplaintResponse> getComplaintsByWorker(Long workerId) {
        return complaintRepository.findByAssignedWorkerId(workerId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StatusHistoryResponse> getStatusHistory(Long complaintId) {
        return historyRepository.findByComplaintIdOrdered(complaintId)
                .stream()
                .map(this::toHistoryResponse)
                .collect(Collectors.toList());
    }

    // ---------------- UPDATE ----------------

    public ComplaintResponse updateStatus(Long id, String status, Long workerId,
                                          String remarks, Long updatedById) {
        Complaint c = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        Status newStatus = Status.valueOf(status.toUpperCase());
        c.setStatus(newStatus);

        if (workerId != null) {
            User worker = userRepository.findById(workerId)
                    .orElseThrow(() -> new RuntimeException("Worker not found"));
            c.setAssignedWorker(worker);
        }

        Complaint saved = complaintRepository.save(c);

        // Log status change
        ComplaintStatusHistory history = new ComplaintStatusHistory();
        history.setComplaint(saved);
        history.setStatus(newStatus);
        history.setRemarks(remarks != null ? remarks : "Status updated to " + newStatus);
        if (updatedById != null) {
            User updater = userRepository.findById(updatedById).orElse(null);
            history.setUpdatedBy(updater);
        }
        historyRepository.save(history);

        return toResponse(saved);
    }

    // ---------------- HELPERS ----------------

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

    private StatusHistoryResponse toHistoryResponse(ComplaintStatusHistory h) {
        StatusHistoryResponse r = new StatusHistoryResponse();
        r.setId(h.getId());
        r.setStatus(h.getStatus());
        r.setRemarks(h.getRemarks());
        r.setCreatedAt(h.getCreatedAt());
        if (h.getUpdatedBy() != null) {
            r.setUpdatedById(h.getUpdatedBy().getId());
            r.setUpdatedByName(h.getUpdatedBy().getName());
        }
        return r;
    }
}