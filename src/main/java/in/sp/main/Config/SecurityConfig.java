package in.sp.main.Config;

import in.sp.main.filter.JwtAuthFilter;
import in.sp.main.services.CustomeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity //we are enable our web security features inside our spring application
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    //    Used by modern application
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // must disable for POST in Postman
                .authorizeHttpRequests(auth -> auth.requestMatchers("/generateToken/authenticate","/api/account/deposite/**").permitAll()
                        .anyRequest().authenticated()
                );
                http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//                .httpBasic(Customizer.withDefaults()); // Basic Auth

        return http.build();
    }


    //Create the Custome AuthenticationManager for DaoAuthenticateProvider
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(daoAuthenticationProvider);
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.httpBasic(withDefaults()); //In modern spring security this is not an use
//        return http.build();
//    }

    //Create Bean for ...
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomeUserDetailsService();
    }

    //Create Bean for PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//    @Bean // Use this for the get transaction manager of JDBC instead of jpa default.
//    public DataSource getDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/ombank_db");
//        dataSource.setUsername("root");
//        dataSource.setPassword("Info@66527");
//        return dataSource;
//    }
//
//    @Bean
//    public TransactionManager transactionManager(DataSource getDataSource) {
//        return new DataSourceTransactionManager(getDataSource());
//    }






}
