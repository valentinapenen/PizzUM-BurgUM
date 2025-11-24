package com.example.PizzUMBurgUM.configuracion;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        
        String errorMessage = "Error de autenticación: ";
        
        // Log detallado del error según el tipo de excepción
        if (exception instanceof BadCredentialsException) {
            errorMessage += "Credenciales incorrectas";
            System.out.println(errorMessage);
            System.out.println("Detalles: " + exception.getMessage());
        } else if (exception instanceof UsernameNotFoundException) {
            errorMessage += "Usuario no encontrado";
            System.out.println(errorMessage);
            System.out.println("Detalles: " + exception.getMessage());
        } else if (exception instanceof LockedException) {
            errorMessage += "Cuenta bloqueada";
            System.out.println(errorMessage);
            System.out.println("Detalles: " + exception.getMessage());
        } else if (exception instanceof DisabledException) {
            errorMessage += "Cuenta deshabilitada";
            System.out.println(errorMessage);
            System.out.println("Detalles: " + exception.getMessage());
        } else {
            errorMessage += exception.getMessage();
            System.out.println("Error de autenticación no específico: " + exception.getClass().getName());
            System.out.println("Detalles: " + exception.getMessage());
            exception.printStackTrace();
        }
        
        // Redirigir a la página de login con el mensaje de error
        request.getSession().setAttribute("error", errorMessage);
        response.sendRedirect(request.getContextPath() + "/iniciar-sesion?error");
    }
}