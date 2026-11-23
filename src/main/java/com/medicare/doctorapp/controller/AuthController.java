
package com.medicare.doctorapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.doctorapp.dto.DoctorRegisterRequest;
import com.medicare.doctorapp.dto.PatientRegisterRequest;
import com.medicare.doctorapp.entity.Doctor;
import com.medicare.doctorapp.entity.Patient;
import com.medicare.doctorapp.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
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
}
