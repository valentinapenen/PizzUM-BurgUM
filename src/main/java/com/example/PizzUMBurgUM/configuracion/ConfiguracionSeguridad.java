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
                // Permisos por tipo de ruta
                .authorizeHttpRequests(auth -> auth
                        // Rutas accesibles solo para administradores
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Rutas accesibles solo para usuarios registrados
                        .requestMatchers("/usuario/**").hasRole("USER")

                        // Rutas públicas (no requieren autenticación)
                        .requestMatchers(
                                "/",                   // raíz → bienvenida
                                "/bienvenida",         // página de bienvenida
                                "/iniciar-sesion",     // login
                                "/crear-cuenta",       // registro
                                "/css/**", "/js/**", "/images/**" // archivos estáticos
                        ).permitAll()

                        // To do lo demás requiere estar logueado
                        .anyRequest().authenticated()
                )

                // Configurar el login
                .formLogin(form -> form
                        .loginPage("/iniciar-sesion")       // Página del formulario de login
                        .loginProcessingUrl("/procesar-login") // Acción del formulario
                        .defaultSuccessUrl("/cliente/inicio-cliente", false) // Redirige después del login exitoso
                        .permitAll()
                )

                // Configurar logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/bienvenida")     // Volver a bienvenida al salir
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Bean para cifrar contraseñas (obligatorio para Spring Security)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
