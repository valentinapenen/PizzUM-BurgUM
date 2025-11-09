package com.example.PizzUMBurgUM.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Autorización: define quién puede acceder a qué rutas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")   // Solo admins
                        .requestMatchers("/usuario/**").hasRole("USER")  // Solo clientes logueados
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/", "/iniciar-sesion", "/registrarse").permitAll() // Acceso libre
                        .anyRequest().authenticated() // El resto requiere login
                )

                // Login
                .formLogin(form -> form
                        .loginPage("/iniciar-sesion")   // URL de tu formulario de login (Thymeleaf)
                        .loginProcessingUrl("/procesar-login") // donde se envía el form
                        .defaultSuccessUrl("/", true)   // a dónde redirige al iniciar sesión correctamente
                        .permitAll()
                )

                // Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")          // vuelve al inicio
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // ⚠️ Protección CSRF (por ahora simple)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Cifrado de contraseñas (obligatorio)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
