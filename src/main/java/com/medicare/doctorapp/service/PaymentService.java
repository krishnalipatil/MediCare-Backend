
package com.medicare.doctorapp.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import com.medicare.doctorapp.dto.AppointmentRequest;
import com.medicare.doctorapp.dto.PaymentVerificationRequest;
import com.medicare.doctorapp.entity.Appointment;
import com.medicare.doctorapp.entity.Doctor;
import com.medicare.doctorapp.repository.DoctorRepository;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final DoctorRepository doctorRepository;
    private final AppointmentService appointmentService;

    public PaymentService(
            DoctorRepository doctorRepository,
            AppointmentService appointmentService) {

        this.doctorRepository = doctorRepository;
        this.appointmentService = appointmentService;
    }

    // Create Razorpay Order
    public Map<String, Object> createOrder(Long doctorId)
            throws RazorpayException {

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        if (!doctor.isActive()) {
            throw new RuntimeException("Doctor is not available");
        }

        BigDecimal fee = new BigDecimal(
                String.valueOf(doctor.getConsultationFee()));

        long amount = fee.movePointRight(2).longValueExact();

        if (amount < 100) {
            throw new RuntimeException("Invalid appointment fee");
        }

        RazorpayClient client =
                new RazorpayClient(keyId, keySecret);

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", amount);
        orderRequest.put("currency", "INR");
        orderRequest.put(
                "receipt",
                UUID.randomUUID().toString().replace("-", "")
        );

        Order order = client.orders.create(orderRequest);

        return Map.of(
                "orderId", order.get("id"),
                "amount", order.get("amount"),
                "currency", order.get("currency"),
                "keyId", keyId
        );
    }

    // Verify Payment and Book Appointment
    public Appointment verifyPayment(
            PaymentVerificationRequest request)
            throws RazorpayException {

        if (request.getAppointmentRequest() == null) {
            throw new RuntimeException(
                    "Appointment details are missing");
        }

        if (request.getRazorpayOrderId() == null ||
                request.getRazorpayPaymentId() == null ||
                request.getRazorpaySignature() == null) {

            throw new RuntimeException(
                    "Payment details are missing");
        }

        // Get appointment request
        AppointmentRequest appointmentRequest =
                request.getAppointmentRequest();

        // Find doctor
        Doctor doctor = doctorRepository.findById(
                appointmentRequest.getDoctorId())
                .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

        // Calculate expected appointment fee
        BigDecimal fee = new BigDecimal(
                String.valueOf(doctor.getConsultationFee()));

        long expectedAmount =
                fee.movePointRight(2).longValueExact();

        // Verify Razorpay signature
        JSONObject paymentDetails = new JSONObject();

        paymentDetails.put(
                "razorpay_order_id",
                request.getRazorpayOrderId());

        paymentDetails.put(
                "razorpay_payment_id",
                request.getRazorpayPaymentId());

        paymentDetails.put(
                "razorpay_signature",
                request.getRazorpaySignature());

        boolean signatureValid =
                Utils.verifyPaymentSignature(
                        paymentDetails,
                        keySecret);

        if (!signatureValid) {
            throw new RuntimeException(
                    "Invalid payment signature");
        }

        // Connect to Razorpay
        RazorpayClient client =
                new RazorpayClient(keyId, keySecret);

        // Fetch payment details
        Payment payment = client.payments.fetch(
                request.getRazorpayPaymentId());

        // Fetch order details
        Order order = client.orders.fetch(
                request.getRazorpayOrderId());

        // Check payment belongs to the selected order
        if (!request.getRazorpayOrderId().equals(
                payment.get("order_id"))) {

            throw new RuntimeException(
                    "Payment does not belong to this order");
        }

        // Check payment amount
        long paidAmount =
                ((Number) payment.get("amount")).longValue();

        long orderAmount =
                ((Number) order.get("amount")).longValue();

        if (paidAmount != expectedAmount ||
                orderAmount != expectedAmount) {

            throw new RuntimeException(
                    "Payment amount mismatch");
        }

        // Check currency
        if (!"INR".equals(payment.get("currency"))) {
            throw new RuntimeException(
                    "Invalid payment currency");
        }

        // Check payment status
        String paymentStatus =
                payment.get("status").toString();

        if (!"captured".equals(paymentStatus)) {
            throw new RuntimeException(
                    "Payment is not captured successfully");
        }

        // Book appointment after payment verification
        return appointmentService.bookAppointment(
                appointmentRequest);
    }
}
