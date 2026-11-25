
package com.medicare.doctorapp.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.doctorapp.dto.DoctorRegisterRequest;
import com.medicare.doctorapp.dto.PatientRegisterRequest;
import com.medicare.doctorapp.dto.LoginRequest;

import com.medicare.doctorapp.entity.Doctor;
import com.medicare.doctorapp.entity.Patient;
import com.medicare.doctorapp.entity.User;

import com.medicare.doctorapp.repository.PatientRepository;
import com.medicare.doctorapp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final PatientRepository patientRepository;

    public AuthController(
            AuthService authService,
            PatientRepository patientRepository) {

        this.authService = authService;
        this.patientRepository = patientRepository;
    }

    // Doctor Registration
    @PostMapping("/register/doctor")
    public ResponseEntity<Doctor> registerDoctor(
            @RequestBody DoctorRegisterRequest request) {

        Doctor doctor = authService.registerDoctor(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(doctor);
    }

    // Patient Registration
    @PostMapping("/register/patient")
    public ResponseEntity<Patient> registerPatient(
            @RequestBody PatientRegisterRequest request) {

        Patient patient = authService.registerPatient(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(patient);
    }

    // User Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {

            User user = authService.login(request);

            Map<String, Object> response = new HashMap<>();

            response.put("id", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
            response.put("role", user.getRole());

            // Fetch Patient ID using User ID
            if (user.getRole().toString().equals("PATIENT")) {

                Optional<Patient> patient =
                        patientRepository.findByUser_Id(user.getId());

                if (patient.isEmpty()) {
                    return ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(Map.of(
                                    "message",
                                    "Patient record not found for this user"
                            ));
                }

                response.put("patientId", patient.get().getId());
            }

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
