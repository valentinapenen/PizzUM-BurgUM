package com.example.PizzUMBurgUM.configuracion;

import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.repositories.UsuarioRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                       Authentication authentication) throws IOException, ServletException {
        
        // Log para depuración
        System.out.println("Autenticación exitosa para usuario: " + authentication.getName());
        
        // Obtener el usuario completo de la base de datos
        Usuario usuario = usuarioRepository.findByCorreo(authentication.getName());
        
        if (usuario != null) {
            // Guardar el usuario en la sesión con el nombre de atributo esperado por AuthController
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogueado", usuario);
            
            System.out.println("Usuario guardado en sesión: " + usuario.getNombre() + " " + usuario.getApellido());
        } else {
            System.out.println("No se pudo encontrar el usuario en la base de datos después de la autenticación");
        }
        
        // Redirigir a la URL de éxito configurada en Spring Security
        response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/post-login"));
    }
}