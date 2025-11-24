package com.example.PizzUMBurgUM.configuracion;

import com.example.PizzUMBurgUM.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        // Todo lo público
                        .requestMatchers(
                                "/", "/bienvenida", "/iniciar-sesion", "/crear-cuenta",
                                "/css/**", "/js/**", "/images/**",
                                "/api/bps/**", "/api/tarjetas/**", "/api/dgi/**"
                        ).permitAll()

                        // Todo lo demás requiere estar logueado
                        .anyRequest().authenticated()
                )

                // Config login
                .formLogin(form -> form
                        .loginPage("/iniciar-sesion")          // Tu formulario
                        .loginProcessingUrl("/procesar-login") // URL del POST
                        .usernameParameter("correo")          // Nombre del campo de correo
                        .passwordParameter("contrasena")      // Nombre del campo de contraseña
                        .successHandler(authenticationSuccessHandler) // Manejador personalizado de éxito
                        .failureHandler(authenticationFailureHandler) // Manejador personalizado de fallo
                        .permitAll()
                )

                // Config logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/bienvenida")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configurar el servicio de usuarios y el codificador de contraseñas
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder);
    }

}
