package com.example.Status.of.application.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
public class OtpService {

    private HashMap<String, String> otpStore = new HashMap<>();

    public String sendOtp(String mobile) {
        String otp = String.valueOf(new Random().nextInt(9000) + 1000);
        otpStore.put(mobile, otp);

        System.out.println("OTP for" + mobile + "is:" + otp);

        return "OTP sent successfully";
    }
    

    public boolean verifyOtp(String mobile, String otp) {
        return otp.equals(otpStore.get(mobile));

    }
}
