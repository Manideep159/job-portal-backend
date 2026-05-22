package com.example.Status.of.application.service;

import com.example.Status.of.application.dto.OtpResponseDTO;
import com.example.Status.of.application.dto.RegisterRequestDTO;
import com.example.Status.of.application.entity.User;
import com.example.Status.of.application.repository.UserRepository;
import com.example.Status.of.application.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class AuthService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final OtpService otpService;
    @Autowired
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, OtpService otpService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.otpService = otpService;
        this.jwtUtil = jwtUtil;
    }

    public String sendOtp(String mobile) {
        userRepository.findByMobileNumber(mobile)
                .orElseThrow(() -> new RuntimeException("Mobile number not registered. Please register first."));

        otpService.sendOtp(mobile);
        return "OTP sent successfully";

    }

    public void register(RegisterRequestDTO request) {

        boolean exists = userRepository
                .findByMobileNumber(request.getMobile())
                .isPresent();

        if (exists) {
            throw new RuntimeException("Mobile number already registered");
        }

        User user = new User();
        user.setMobileNumber(request.getMobile());
        user.setName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setLocation(request.getLocation());
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    public OtpResponseDTO verifyOtp(
            String mobile,
            String otp
    ) {

        boolean isValid = otpService.verifyOtp(
                mobile,
                otp
        );

        if (!isValid) {
            throw new RuntimeException("Invalid OTP");
        }

        User user = userRepository
                .findByMobileNumber(mobile)
                .orElseGet(() -> {

                    User newUser = new User();

                    newUser.setMobileNumber(mobile);

                    newUser.setName("User");

                    newUser.setRole("ROLE_USER");

                    return userRepository.save(newUser);
                });

        String token = jwtUtil.generateToken(
                user.getMobileNumber()
        );

        System.out.println("GENERATED TOKEN: " + token);

        return new OtpResponseDTO(
                token,
                user.getRole()
        );
    }

//    public OtpResponseDTO verifyOtp(String mobile, String otp) {
//
//        if (!otpService.verifyOtp(mobile, otp)) {
//            throw new RuntimeException("Invalid OTP");
//        }
//
//        // ✅ Check if user exists
//        User user = userRepository.findByMobileNumber(mobile)
//                .orElseGet(() -> {
//                    User newUser = new User();
//                    newUser.setMobileNumber(mobile);
//                    newUser.setName("User");
//                    newUser.setRole("ROLE_USER");
//                    return userRepository.save(newUser);
//                });
//        String token = jwtUtil.generateToken(user.getMobileNumber());
//
//        return new OtpResponseDTO(token, user.getRole());
//
//
//        // ✅ Generate token with mobile
////        return jwtUtil.generateToken(user.getMobileNumber());
//    }
//
////        // ✅ Generate JWT
////        return jwtUtil.generateToken(mobile);
////    }


}
