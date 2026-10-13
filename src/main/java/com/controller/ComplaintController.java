package com.campuscare.campuscare.controller;

import com.campuscare.campuscare.entity.Complaint;
import com.campuscare.campuscare.repository.ComplaintRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "${app.cors.allowed-origins}")
public class ComplaintController {

    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    // CREATE COMPLAINT
    @PostMapping
    public Complaint createComplaint(@RequestBody Complaint complaint) {

        complaint.setStatus("SUBMITTED");
        complaint.setCreatedAt(LocalDateTime.now());

        return complaintRepository.save(complaint);
    }

    // GET ALL COMPLAINTS
    @GetMapping
    public List<Complaint> getAllComplaints() {

        return complaintRepository.findAll();
    }

    // GET COMPLAINT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(
            @PathVariable Long id) {

        return complaintRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<Complaint> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return complaintRepository.findById(id)
                .map(complaint -> {

                    complaint.setStatus(status);

                    Complaint updated =
                            complaintRepository.save(complaint);

                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE COMPLAINT
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComplaint(
            @PathVariable Long id) {

        if (!complaintRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        complaintRepository.deleteById(id);

        return ResponseEntity.ok(
                "Complaint deleted successfully"
        );
    }
}
