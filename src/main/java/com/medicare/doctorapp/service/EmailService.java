
package com.medicare.doctorapp.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendAppointmentConfirmation(
            String patientEmail,
            String patientName,
            String doctorName,
            String appointmentDate,
            String appointmentTime,
            String status) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(patientEmail);

        message.setSubject(
            "MediCare - Appointment Confirmation"
        );

        message.setText(
            "Dear " + patientName + ",\n\n" +

            "Your appointment has been successfully booked!\n\n" +

            "Appointment Details:\n" +
            "Doctor: " + doctorName + "\n" +
            "Date: " + appointmentDate + "\n" +
            "Time: " + appointmentTime + "\n" +
            "Status: " + status + "\n\n" +

            "Thank you for choosing MediCare.\n\n" +
            "Regards,\n" +
            "MediCare Team"
        );

        mailSender.send(message);
    }
}
