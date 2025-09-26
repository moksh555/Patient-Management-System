package org.pms.patientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.pms.patientservice.dto.PatientRequestDTO;
import org.pms.patientservice.dto.PatientResponseDTO;
import org.pms.patientservice.dto.validators.CreatePatientValidationGroup;
import org.pms.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name="Patient", description="API for managing Patients")
public class PatientController {

    private final PatientService patientService;


    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary="Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
         List<PatientResponseDTO> patients = patientService.getPatients();
         return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    @Operation(summary="Create a new Patients")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO newPatientDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(newPatientDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary="Update a Patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO updatedPatient = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.ok().body(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Delete a Patients")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
