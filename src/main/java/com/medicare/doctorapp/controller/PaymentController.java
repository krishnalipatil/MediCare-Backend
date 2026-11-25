
package com.medicare.doctorapp.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.medicare.doctorapp.dto.PaymentVerificationRequest;
import com.medicare.doctorapp.service.PaymentService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // Create Razorpay Order
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(
            @RequestParam Long doctorId)
            throws RazorpayException {

        return paymentService.createOrder(doctorId);
    }

    // Verify Payment and Book Appointment
    @PostMapping("/verify")
    public Object verifyPayment(
            @RequestBody PaymentVerificationRequest request)
            throws RazorpayException {

        return paymentService.verifyPayment(request);
    }
}
