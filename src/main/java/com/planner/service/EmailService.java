package com.planner.service;

import org.springframework.beans.factory.annotation.Value;
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
        body.put("from", "onboarding@resend.dev");
        body.put("to", toEmail);
        body.put("subject", "Daily Planner Password Reset OTP");
        body.put("text", "Your OTP is: " + otp + "\nValid for 5 minutes.");

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        org.springframework.http.HttpEntity<Map<String, Object>> entity =
                new org.springframework.http.HttpEntity<>(body, headers);

        restTemplate.postForEntity(url, entity, String.class);
    }
}
