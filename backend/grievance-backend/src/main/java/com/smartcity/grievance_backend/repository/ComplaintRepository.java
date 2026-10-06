package com.smartcity.grievance_backend.repository;

import com.smartcity.grievance_backend.entity.Complaint;
import com.smartcity.grievance_backend.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByCitizenId(Long citizenId);

    List<Complaint> findByStatus(Status status);

    List<Complaint> findByDepartment(String department);

    List<Complaint> findByAssignedWorkerId(Long workerId);

    List<Complaint> findByWard(String ward);
}