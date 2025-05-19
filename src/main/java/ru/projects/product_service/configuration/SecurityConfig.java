package ru.projects.product_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.projects.product_service.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST,"/api/attributes", "/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/attributes", "/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/attributes", "/api/categories").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/products", "/api/products/variations").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.PUT,"/api/products", "/api/products/variations").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.DELETE,"/api/products", "/api/products/variations").hasAnyRole("ADMIN", "SELLER")
                        .requestMatchers(HttpMethod.GET).permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
