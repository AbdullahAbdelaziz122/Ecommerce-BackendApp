package com.BMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())


                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin/**").authenticated()
                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().permitAll()
                )


                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )


                .httpBasic(withDefaults());
        return http.build();
    }

}
