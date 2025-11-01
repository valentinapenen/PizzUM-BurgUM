package com.example.PizzUMBurgUM.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    @Bean
    public SecurityFilterChain cadenaDeFiltrosDeSeguridad(HttpSecurity http) throws Exception {
        http
                // Permitir acceso a todas las páginas sin autenticación
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // Permite todo sin login
                )
                // Desactivar CSRF (protección contra ataques)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}