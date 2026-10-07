package com.smartcity.grievance_backend.repository;

import com.smartcity.grievance_backend.entity.ComplaintStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintStatusHistoryRepository extends JpaRepository<ComplaintStatusHistory, Long> {

    @Query("SELECT h FROM ComplaintStatusHistory h " +
            "LEFT JOIN FETCH h.updatedBy " +
            "WHERE h.complaint.id = :complaintId " +
            "ORDER BY h.createdAt ASC")
    List<ComplaintStatusHistory> findByComplaintIdOrdered(@Param("complaintId") Long complaintId);
}