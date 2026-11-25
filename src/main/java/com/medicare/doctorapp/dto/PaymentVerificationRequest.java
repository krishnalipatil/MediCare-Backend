
package com.medicare.doctorapp.dto;

public class PaymentVerificationRequest {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;

    private AppointmentRequest appointmentRequest;

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }

    public AppointmentRequest getAppointmentRequest() {
        return appointmentRequest;
    }

    public void setAppointmentRequest(
            AppointmentRequest appointmentRequest) {
        this.appointmentRequest = appointmentRequest;
    }
}
