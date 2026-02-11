package in.sp.main.services;

import in.sp.main.model.Users;
import in.sp.main.repository.UserDetailsRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer  {

    @Bean
    public ApplicationRunner applicationRunner(UserDetailsRepository userDetailsRepository, PasswordEncoder passwordEncoder){
        return args -> {
            System.out.println("Application Runner Executed...");
            Users user = new Users();

            if (userDetailsRepository.findByUsername("user").isEmpty()) {
                user.setUsername("Shraddha");
                user.setPassword(passwordEncoder.encode("Namaste")); // Securely store password
                user.setRole("ROLE_USER");

                userDetailsRepository.save(user);
                System.out.println("Default user created! By Application Ruuner");
            }
        };
    }
}
