package com.medicare.doctorapp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medicare.doctorapp.entity.Doctor;
import com.medicare.doctorapp.repository.DoctorRepository;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    // Constructor Injection
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    // 1. Add Doctor
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // 2. Get All Doctors
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    // 3. Get Doctor By ID
    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() ->
                new RuntimeException("Doctor not found with ID: " + id));
    }

    // 4. Update Doctor
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {

        Doctor existingDoctor = getDoctorById(id);

        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setExperience(updatedDoctor.getExperience());
        existingDoctor.setConsultationFee(updatedDoctor.getConsultationFee());
        existingDoctor.setLocation(updatedDoctor.getLocation());
        existingDoctor.setQualification(updatedDoctor.getQualification());

        return doctorRepository.save(existingDoctor);
    }

    // 5. Deactivate Doctor
    public Doctor deactivateDoctor(Long id) {

        Doctor doctor = getDoctorById(id);

        doctor.setActive(false);

        return doctorRepository.save(doctor);
    }
    
 // Search Doctors by Specialization
    public List<Doctor> searchDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationIgnoreCaseAndActiveTrue(specialization);
    }
 // Search Doctors by Location
    public List<Doctor> searchDoctorsByLocation(String location) {
        return doctorRepository.findByLocationIgnoreCaseAndActiveTrue(location);
    }

}