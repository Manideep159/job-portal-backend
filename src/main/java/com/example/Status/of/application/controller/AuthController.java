package com.example.Status.of.application.controller;

import com.example.Status.of.application.dto.OtpRequestDTO;
import com.example.Status.of.application.dto.OtpResponseDTO;
import com.example.Status.of.application.dto.RegisterRequestDTO;
import com.example.Status.of.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;


    @PostMapping("/send-otp")
    private String sendOtp(@RequestParam String mobile) {
        return authService.sendOtp(mobile);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    //    @PostMapping("/verify-otp")
//    private String verifyOtp(@RequestParam String mobile, @RequestParam String otp) {
//        return authService.verifyOtp(mobile, otp);
//    }
//    @PostMapping("/verify-otp")
//    public OtpResponseDTO verifyOtp(@RequestBody OtpRequestDTO request) {
////        String token = authService.verifyOtp(request.getMobile(), request.getOtp());
//        return new OtpResponseDTO(request.getMobile(), request.getOtp());
//    }
    @PostMapping("/verify-otp")
    public OtpResponseDTO verifyOtp(
            @RequestBody OtpRequestDTO request
    ) {

        return authService.verifyOtp(
                request.getMobile(),
                request.getOtp()
        );
    }


    public void AuthController(AuthService authService) {
        this.authService = authService;
    }
}
