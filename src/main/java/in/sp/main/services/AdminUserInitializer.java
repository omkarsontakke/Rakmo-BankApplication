package in.sp.main.services;

import in.sp.main.model.Role;
import in.sp.main.model.Users;
import in.sp.main.repository.UserDetailsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer {

    @Bean
    public CommandLineRunner createAdminUser(UserDetailsRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            System.out.println(userRepository.findByUsername("admin").isEmpty());
            Users admin = new Users();
            if (userRepository.findByUsername("admin").isEmpty()) {

                admin.setUsername("allied");
                admin.setPassword(passwordEncoder.encode("allied123")); // Securely store password
                admin.setRole("ROLE_ADMIN");

                userRepository.save(admin);
                System.out.println("Default admin user created!");
            }
//            if (userRepository.findByUsername("user").isEmpty()) {
//                admin.setUsername("omkar");
//                admin.setPassword(passwordEncoder.encode("rakmo")); // Securely store password
//                admin.setRole("ROLE_USER");
//
//                userRepository.save(admin);
//                System.out.println("Default user created!");
//            }

        };
    }
}
