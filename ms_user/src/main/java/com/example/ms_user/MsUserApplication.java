package com.example.ms_user;

import com.example.ms_user.model.Role;
import com.example.ms_user.model.User;
import com.example.ms_user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MsUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsUserApplication.class, args);
	}
    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@system.com")) {
                User admin = new User();
                admin.setNom("ADMIN");
                admin.setPrenom("SYSTEM");
                admin.setEmail("admin@system.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);
                admin.setEnabled(true);

                userRepository.save(admin);
            }
        };
    }
}
