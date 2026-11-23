package com.medicare.doctorapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicare.doctorapp.entity.Doctor;
import com.medicare.doctorapp.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin("http://localhost:5173/")
public class DoctorController {

    private final DoctorService doctorService;

    // Constructor Injection
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    // 1. Add Doctor
    @PostMapping
    public Doctor addDoctor(@RequestBody Doctor doctor) {
        return doctorService.addDoctor(doctor);
    }

    // 2. Get All Doctors
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    // 3. Get Doctor By ID
    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    // 4. Update Doctor
    @PutMapping("/{id}")
    public Doctor updateDoctor(
            @PathVariable Long id,
            @RequestBody Doctor doctor) {

        return doctorService.updateDoctor(id, doctor);
    }

    // 5. Deactivate Doctor
    @PutMapping("/{id}/deactivate")
    public Doctor deactivateDoctor(@PathVariable Long id) {
        return doctorService.deactivateDoctor(id);
    }
    
 // Search Doctors by Specialization
    @GetMapping("/search")
    public List<Doctor> searchDoctors(
            @RequestParam("specialization") String specialization) {

        return doctorService.searchDoctorsBySpecialization(specialization);
    }
    
 // Search Doctors by Location
    @GetMapping("/location")
    public List<Doctor> searchDoctorsByLocation(@RequestParam("location") String location) {
        return doctorService.searchDoctorsByLocation(location);
    }


}


