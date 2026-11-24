
package com.medicare.doctorapp.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.medicare.doctorapp.entity.User;
import com.medicare.doctorapp.repository.UserRepository;

@Configuration
public class DoctorPasswordReset {

    @Bean
    public CommandLineRunner resetDoctorPassword(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            User user = userRepository
                    .findByEmail("rahul.mehta2026@gmail.com")
                    .orElseThrow(() ->
                        new RuntimeException("Doctor not found"));

            user.setPassword(
                passwordEncoder.encode("Rahul@12345")
            );

            userRepository.save(user);

            System.out.println("Doctor password reset successfully!");
        };
    }
}
