package com.example.Status.of.application.security;

import com.example.Status.of.application.entity.User;
import com.example.Status.of.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
//@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userName) {

        User user = userRepository.findByMobileNumber(userName)
                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getMobileNumber())
                .password("") // no password
                .authorities(user.getRole())
                .build();
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        return userRepository.findByMobileNumber(username)
//                .map(user -> new org.springframework.security.core.userdetails.User(
//                        user.getMobileNumber(),
//                        "",
//                        List.of(new SimpleGrantedAuthority(user.getRole()))
//                ))
//                .orElseThrow(() -> new UsernameNotFoundException("USER NOT FOUND"));
//    }
}
