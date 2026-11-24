
package com.medicare.doctorapp.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medicare.doctorapp.dto.DoctorRegisterRequest;
import com.medicare.doctorapp.dto.PatientRegisterRequest;
import com.medicare.doctorapp.dto.LoginRequest;

import com.medicare.doctorapp.entity.Doctor;
import com.medicare.doctorapp.entity.Patient;
import com.medicare.doctorapp.entity.User;

import com.medicare.doctorapp.enums.Role;

import com.medicare.doctorapp.repository.DoctorRepository;
import com.medicare.doctorapp.repository.PatientRepository;
import com.medicare.doctorapp.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Doctor Registration
    @Transactional
    public Doctor registerDoctor(DoctorRegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.DOCTOR);

        User savedUser = userRepository.save(user);

        // Check whether password was encoded correctly
        boolean checkPassword = passwordEncoder.matches(
                request.getPassword(),
                savedUser.getPassword()
        );

        System.out.println("Doctor Registration Password Check: " + checkPassword);

        Doctor doctor = new Doctor();

        doctor.setUser(savedUser);
        doctor.setSpecialization(request.getSpecialization());
        doctor.setExperience(request.getExperience());
        doctor.setConsultationFee(request.getConsultationFee());
        doctor.setLocation(request.getLocation());
        doctor.setQualification(request.getQualification());

        return doctorRepository.save(doctor);
    }

    // Patient Registration
    @Transactional
    public Patient registerPatient(PatientRegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.PATIENT);

        User savedUser = userRepository.save(user);

        // Check whether password was encoded correctly
        boolean checkPassword = passwordEncoder.matches(
                request.getPassword(),
                savedUser.getPassword()
        );

        System.out.println("Patient Registration Password Check: " + checkPassword);

        Patient patient = new Patient();

        patient.setUser(savedUser);
        patient.setAge(request.getAge());
        patient.setGender(request.getGender());
        patient.setAddress(request.getAddress());

        return patientRepository.save(patient);
    }

    // User Login
    public User login(LoginRequest request) {

        System.out.println("Entered Email: " + request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    System.out.println("Email not found!");
                    return new RuntimeException("Invalid email or password");
                });

        System.out.println("User Found: " + user.getName());
        System.out.println("User Role: " + user.getRole());

        boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        System.out.println("Password Match: " + passwordMatch);

        if (!passwordMatch) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
}
