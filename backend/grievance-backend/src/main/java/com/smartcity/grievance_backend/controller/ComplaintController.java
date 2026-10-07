package com.smartcity.grievance_backend.controller;
import com.smartcity.grievance_backend.dto.StatusHistoryResponse;
import com.smartcity.grievance_backend.dto.ComplaintRequest;
import com.smartcity.grievance_backend.dto.ComplaintResponse;
import com.smartcity.grievance_backend.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping
    public ResponseEntity<ComplaintResponse> create(@RequestBody ComplaintRequest request) {
        return ResponseEntity.ok(complaintService.createComplaint(request));
    }

    @GetMapping
    public List<ComplaintResponse> getAll() {
        return complaintService.getAllComplaints();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(complaintService.getComplaintById(id));
    }

    @GetMapping("/citizen/{citizenId}")
    public List<ComplaintResponse> getByCitizen(@PathVariable Long citizenId) {
        return complaintService.getComplaintsByCitizen(citizenId);
    }

    @GetMapping("/department/{department}")
    public List<ComplaintResponse> getByDepartment(@PathVariable String department) {
        return complaintService.getComplaintsByDepartment(department);
    }

    @GetMapping("/worker/{workerId}")
    public List<ComplaintResponse> getByWorker(@PathVariable Long workerId) {
        return complaintService.getComplaintsByWorker(workerId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ComplaintResponse> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        String status = (String) body.get("status");
        Long workerId = body.get("workerId") != null
                ? Long.valueOf(body.get("workerId").toString())
                : null;
        String remarks = (String) body.get("remarks");
        Long updatedById = body.get("updatedById") != null
                ? Long.valueOf(body.get("updatedById").toString())
                : null;

        return ResponseEntity.ok(
                complaintService.updateStatus(id, status, workerId, remarks, updatedById)
        );
    }

    @GetMapping("/{id}/history")
    public List<StatusHistoryResponse> getHistory(@PathVariable Long id) {
        return complaintService.getStatusHistory(id);
    }
}