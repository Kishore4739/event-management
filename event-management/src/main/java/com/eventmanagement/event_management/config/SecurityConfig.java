package com.eventmanagement.event_management.config;

import com.eventmanagement.event_management.oauth.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/", "/login", "/login.html", "/register.html",
//                                "/css/**", "/js/**", "/images/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .oauth2Login(oauth -> oauth
//                        .loginPage("/login.html")
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(customOAuth2UserService) // ✅ attach service
//                        )
//                        .defaultSuccessUrl("/dashboard.html", true)
//                )
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/login.html")
//                        .permitAll()
//                )
//                .csrf(csrf -> csrf.disable()); // CSRF disabled for testing
//
//        return http.build();
//    }
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable()) // disable CSRF for Postman
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/users/**", "/api/events/**").permitAll() // allow APIs
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                    .loginPage("/login.html")
                    .defaultSuccessUrl("/dashboard.html", true)
            );

    return http.build();
}

}
