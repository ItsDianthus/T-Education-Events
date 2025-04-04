package com.t_educational.t_edu_events.initializer;

//import com.t_educational.t_edu_events.model.account.Role;
//import com.t_educational.t_edu_events.repository.RoleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class DataInit implements CommandLineRunner {
//
//    private final RoleRepository roleRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Добавляем роль ROLE_USER
//        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
//            Role userRole = new Role();
//            userRole.setName("ROLE_USER");
//            roleRepository.save(userRole);
//        }
//
//        // Добавляем роль ROLE_ADMIN
//        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
//            Role adminRole = new Role();
//            adminRole.setName("ROLE_ADMIN");
//            roleRepository.save(adminRole);
//        }
//    }
//}