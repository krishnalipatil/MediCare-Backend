
package com.medicare.doctorapp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.medicare.doctorapp.dto.AppointmentRequest;
import com.medicare.doctorapp.entity.Appointment;
import com.medicare.doctorapp.enums.AppointmentStatus;
import com.medicare.doctorapp.service.AppointmentService;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:5173")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Book Appointment
    @PostMapping
    public Appointment bookAppointment(
            @RequestBody AppointmentRequest request) {

        return appointmentService.bookAppointment(request);
    }

    // Get All Appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {

        return appointmentService.getAllAppointments();
    }

    // Get Appointment By ID
    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {

        return appointmentService.getAppointmentById(id);
    }

    // Get Appointments By Patient ID
    @GetMapping("/patient/{patientId}")
    public List<Appointment> getPatientAppointments(
            @PathVariable Long patientId) {

        return appointmentService.getPatientAppointments(patientId);
    }

    // Update Appointment Status
    @PutMapping("/{id}/status")
    public Appointment updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam AppointmentStatus status) {

        return appointmentService.updateAppointmentStatus(id, status);
    }

    // Cancel Appointment
    @PutMapping("/{id}/cancel")
    public Appointment cancelAppointment(@PathVariable Long id) {

        return appointmentService.cancelAppointment(id);
    }
}
