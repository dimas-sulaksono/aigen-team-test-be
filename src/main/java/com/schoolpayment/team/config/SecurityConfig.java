package com.schoolpayment.team.config;

import com.schoolpayment.team.exception.JwtAuthenticationEntryPoint;
import com.schoolpayment.team.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    private final UserService userService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, UserService userService,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.userService = userService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(coreCustomizer -> coreCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                        corsConfiguration.setMaxAge(3600L);
                        return corsConfiguration;
                    }
                }))
                .authorizeHttpRequests(session -> session

                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        // user
                        .requestMatchers(HttpMethod.GET, "/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/user/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/user/delete/**").permitAll()

                        // payment
                        .requestMatchers(HttpMethod.GET, "/api/payment-type").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/payment-type/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/payment-type/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/payment-type/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/payment/filter").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payment/search").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payment/all").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payment/get-amount-paid").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payment/get-amount-pending").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payment/name/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/payment/*").authenticated() // get by id and add payment
                        .requestMatchers(HttpMethod.PUT, "/api/payment/*").hasRole("ADMIN")

                        // class
                        .requestMatchers(HttpMethod.POST, "/api/class/add").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/class/update/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/class/soft-delete/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/class/delete/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/class/*").authenticated() // get all and search

                        // student

                        .requestMatchers(HttpMethod.GET, "/api/student/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/student/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/student/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/student/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/student/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/student/get-count-students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/student/**").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/student/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/api/student/**").permitAll()


                        .requestMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/product/**").permitAll()

                        // school years
                        .requestMatchers(HttpMethod.GET, "/api/school-years/all").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/school-years/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/school-years/add").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/school-years/update").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/school-years/soft-delete").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/school-years/delete").permitAll()

                        .anyRequest().authenticated()
                // .anyRequest().permitAll()
                )
                // ngatur session untuk tidak menyimpan informasi user di dalam session tapi
                // pake jwt
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // method bua otentikasi user
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // ngambil data user dari database
    @Bean
    public UserDetailsService userDetailService() {
        return userService::loadUserByUsername;
    }

    // buat nge encode passwors
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}
