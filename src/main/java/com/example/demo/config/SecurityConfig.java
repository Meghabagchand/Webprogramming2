package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				// TODO Auto-generated method stub
				User user=  userRepository.findByUsername(username);
				return user;

			}
		});
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		System.out.println(passwordEncoder().encode("m123"));
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(requests -> requests
						.requestMatchers("/register", "/login", "/","/error")
						.permitAll()

//						.requestMatchers("/hotel/**", "/user/**").hasRole("ADMIN")
//						.requestMatchers("/rating/**")
//						.hasRole("USER")
						.anyRequest().authenticated())

				.formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/")).build();
	}

//	@Bean
//	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//		return http.csrf(csrf -> csrf.disable())
//				.authorizeHttpRequests(
//						requests -> requests
//								.requestMatchers("/login", "/validatelogin/**", "/css/**", "image/**",
//										"/hotel/fetch/**", "/hotel/fetch-hotels/**", "/logout")
//								.permitAll().anyRequest().authenticated())
//
//				.formLogin(f -> f.loginPage("/login")).build();
//	}

	@Bean
	static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
//		return NoOpPasswordEncoder.getInstance();
	}

}