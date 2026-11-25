
package com.medicare.doctorapp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medicare.doctorapp.dto.AppointmentRequest;
import com.medicare.doctorapp.entity.Appointment;
import com.medicare.doctorapp.entity.Doctor;
import com.medicare.doctorapp.entity.Patient;
import com.medicare.doctorapp.enums.AppointmentStatus;
import com.medicare.doctorapp.repository.AppointmentRepository;
import com.medicare.doctorapp.repository.DoctorRepository;
import com.medicare.doctorapp.repository.PatientRepository;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    public AppointmentService(
            AppointmentRepository appointmentRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository) {

        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // Book Appointment
    public Appointment bookAppointment(AppointmentRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (!doctor.isActive()) {
            throw new RuntimeException("Doctor is not available");
        }

        if (request.getAppointmentDate() == null ||
                request.getAppointmentDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Please select a valid appointment date");
        }

        if (request.getAppointmentTime() == null) {
            throw new RuntimeException("Please select appointment time");
        }

        if (request.getReason() == null || request.getReason().isBlank()) {
            throw new RuntimeException("Please enter appointment reason");
        }

        // Check whether the selected slot is already booked
        boolean slotExists =
                appointmentRepository.existsByDoctor_IdAndAppointmentDateAndAppointmentTime(
                        doctor.getId(),
                        request.getAppointmentDate(),
                        request.getAppointmentTime()
                );

        if (slotExists) {
            throw new RuntimeException("This appointment slot is already booked");
        }

        Appointment appointment = new Appointment();

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setReason(request.getReason());
        appointment.setStatus(AppointmentStatus.PENDING);

        return appointmentRepository.save(appointment);
    }

    // Get All Appointments
    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAll();
    }

    // Get Appointment By ID
    public Appointment getAppointmentById(Long id) {

        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    // Get Appointments By Patient ID
    public List<Appointment> getPatientAppointments(Long patientId) {

        return appointmentRepository.findByPatient_Id(patientId);
    }

    // Approve or Reject Appointment
    public Appointment updateAppointmentStatus(
            Long id, AppointmentStatus status) {

        Appointment appointment = getAppointmentById(id);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending appointments can be updated");
        }

        if (status != AppointmentStatus.APPROVED &&
                status != AppointmentStatus.REJECTED) {
            throw new RuntimeException("Invalid appointment status");
        }

        appointment.setStatus(status);

        return appointmentRepository.save(appointment);
    }

    // Cancel Appointment
    public Appointment cancelAppointment(Long id) {

        Appointment appointment = getAppointmentById(id);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED ||
                appointment.getStatus() == AppointmentStatus.REJECTED ||
                appointment.getStatus() == AppointmentStatus.COMPLETED) {

            throw new RuntimeException(
                    "This appointment cannot be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);

        return appointmentRepository.save(appointment);
    }
}
