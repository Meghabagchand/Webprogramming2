package com.example.meghaProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

        @Autowired
        private UserDetailsService userDetailsService;

        @Bean
        AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(new BCryptPasswordEncoder(8));
                provider.setUserDetailsService(userDetailsService);
                return provider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                        throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/", "/login", "/register")
                                                .permitAll()
                                                .requestMatchers("/dashboard/**").hasRole("ADMIN")
                                                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                                                .anyRequest().authenticated())
                                .formLogin(form -> form.loginPage("/login")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/login?error=true").permitAll())
                                .logout(logout -> logout
                                                .logoutSuccessUrl("/login"));
                ;

                return http.build();
        }

}
