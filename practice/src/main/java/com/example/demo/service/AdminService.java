package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.repository.Adminrepository;

@Service
public class AdminService {
    @Autowired
    private Adminrepository adminRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void register(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        LocalDateTime now=LocalDateTime.now();
        admin.setCreatedAt(now);
        admin.setUpdatedAt(now);
        admin.setCurrentSignInAt(now);
        adminRepo.save(admin);
    }

    public Optional<Admin> login(String email, String rawPassword) {
        Optional<Admin> adminOpt = adminRepo.findByEmail(email);
        if (adminOpt.isPresent() && passwordEncoder.matches(rawPassword, adminOpt.get().getPassword())) {
        	Admin admin=adminOpt.get();
        	admin.setCurrentSignInAt(LocalDateTime.now());
        	adminRepo.save(admin);
            return Optional.of(admin);
        }
        return Optional.empty();
    }
}

