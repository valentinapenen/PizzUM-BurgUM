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
                .authorizeHttpRequests(auth -> auth


                        .requestMatchers("/admin/**").hasRole("ADMIN")


                        .requestMatchers("/usuario/**").hasRole("USER")

                        // Webservice BPS (libre)
                        .requestMatchers("/api/bps/**").permitAll()

                        //Webservice TARJETAS (libre)
                        .requestMatchers("/api/tarjetas/**").permitAll()

                        // Webservice DGI
                        .requestMatchers("/api/dgi/**").permitAll()


                        .requestMatchers(
                                "/",
                                "/bienvenida",
                                "/iniciar-sesion",
                                "/crear-cuenta",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()


                        .anyRequest().authenticated()
                )

                // CONFIGURACIÓN DEL LOGIN
                .formLogin(form -> form
                        .loginPage("/iniciar-sesion")
                        .loginProcessingUrl("/procesar-login")
                        .defaultSuccessUrl("/cliente/inicio-cliente", false)
                        .permitAll()
                )

                // CONFIGURACIÓN DEL LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/bienvenida")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // Desactivar CSRF para permitir usar Postman / webservices externos
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    // Password encoder obligatorio para Spring Security
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
