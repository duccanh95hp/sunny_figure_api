package com.example.be.job;

import com.example.be.entity.ERole;
import com.example.be.entity.Role;
import com.example.be.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Danh sách các role cần tạo
        List<ERole> roles = List.of(ERole.ROLE_USER, ERole.ROLE_MODERATOR, ERole.ROLE_ADMIN);

        for (ERole role : roles) {
            if (roleRepository.findByName(role).isEmpty()) {
                Role newRole = new Role(role);
                roleRepository.save(newRole);
            }
        }
    }
}

