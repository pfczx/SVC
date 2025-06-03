package com.example.SVC.config;

import com.example.SVC.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.SVC.model.AppUser;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
//@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private final AppUserService appUserService;


    @Bean
    public UserDetailsService userDetailsService(){
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
//        return httpSecurity
//                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(httpForm ->{
//                    httpForm.loginPage("/req/login").permitAll();
//                    httpForm.defaultSuccessUrl("/dashboard");
//
//                })
//
//
//                .authorizeHttpRequests(registry ->{
//                    registry.requestMatchers("/req/register","/css/**","/js/**").permitAll();
//                    registry.anyRequest().authenticated();
//                })
//                .build();
//    }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(registry -> {
                registry.anyRequest().permitAll(); // Allow ALL requests
            })
            .formLogin(AbstractHttpConfigurer::disable) // Explicitly disable form login
            .logout(AbstractHttpConfigurer::disable) // Disable logout
            .httpBasic(AbstractHttpConfigurer::disable) // Disable basic auth
            .build();
}

}