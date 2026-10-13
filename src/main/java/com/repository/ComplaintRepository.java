package com.campuscare.campuscare.repository;

import com.campuscare.campuscare.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

}