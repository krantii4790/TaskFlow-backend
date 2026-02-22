package com.planner.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${RESEND_API_KEY}")
    private String apiKey;

    public void sendOtpEmail(String toEmail, String otp) {

        String url = "https://api.resend.com/emails";

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("from", "TaskFlow <onboarding@resend.dev>");
        body.put("to", toEmail);
        body.put("subject", "Daily Planner Password Reset OTP");
        body.put("text", "Your OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            System.out.println("Resend response: " + response.getBody());

        } catch (Exception e) {
            System.out.println("Email sending failed: " + e.getMessage());
            throw new RuntimeException("Failed to send OTP email");
        }
    }
}