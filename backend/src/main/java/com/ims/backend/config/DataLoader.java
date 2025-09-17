package com.ims.backend.config;

import com.ims.backend.model.Role;
import com.ims.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if roles already exist
        if (roleRepository.count() == 0) {
            Role admin = new Role();
            admin.setName("ADMIN");

            Role manager = new Role();
            manager.setName("MANAGER");

            Role staff = new Role();
            staff.setName("STAFF");

            roleRepository.save(admin);
            roleRepository.save(manager);
            roleRepository.save(staff);

            System.out.println("Roles pre-populated: ADMIN, MANAGER, STAFF");
        }
    }
}
