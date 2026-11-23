package com.medicare.doctorapp.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medicare.doctorapp.entity.Appointment;

public interface AppointmentRepository  extends  JpaRepository<Appointment,Long> {
	boolean existsByDoctor_IdAndAppointmentDateAndAppointmentTime(
            Long doctorId,
            LocalDate appointmentDate,
            LocalTime appointmentTime
    );

  List<Appointment> findByPatient_Id(Long patientId);

}
