package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.DomicilioRequest;
import com.example.PizzUMBurgUM.controllers.DTOS.LoginRequest;
import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.controllers.DTOS.TarjetaRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.services.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/iniciar-sesion")
    public String mostrarLogin(Model model, HttpSession session) {
        // Añadir el objeto LoginRequest al modelo
        LoginRequest loginRequest = new LoginRequest();
        model.addAttribute("loginRequest", loginRequest);

        // Verificar si hay un mensaje de error en la sesión
        String error = (String) session.getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
            // Limpiar el mensaje de error de la sesión
            session.removeAttribute("error");
            System.out.println("Mostrando mensaje de error: " + error);
        }

        // Devuelve el archivo: src/main/resources/templates/inicio/iniciar-sesion.html
        return "inicio/iniciar-sesion";
    }

    @GetMapping("/crear-cuenta")
    public String mostrarRegistro(Model model) {
        // Crear un nuevo objeto RegistroClienteRequest
        RegistroClienteRequest registroClienteRequest = new RegistroClienteRequest();

        // Inicializar los objetos anidados
        DomicilioRequest domicilio = new DomicilioRequest();
        domicilio.setPredeterminado(true);
        registroClienteRequest.setDomicilio(domicilio);

        TarjetaRequest tarjeta = new TarjetaRequest();
        tarjeta.setPredeterminada(true);
        registroClienteRequest.setTarjeta(tarjeta);

        // Añadir el objeto al modelo
        model.addAttribute("registroClienteRequest", registroClienteRequest);

        // Devuelve el archivo: src/main/resources/templates/inicio/crear-cuenta.html
        return "inicio/crear-cuenta";
    }

    @PostMapping("/crear-cuenta")
    public String procesarRegistro(@Valid @ModelAttribute("registroClienteRequest") RegistroClienteRequest registroRequest,
                                   Model model, RedirectAttributes redirectAttributes) {
        try {
            clienteService.registrarCliente(registroRequest);
            redirectAttributes.addFlashAttribute("exito", "Cuenta creada exitosamente, ahora puede iniciar sesión.");
            return "redirect:/iniciar-sesion";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "inicio/crear-cuenta";
        }
    }

    @GetMapping("/post-login")
    public String postLogin(HttpSession session) {
        // Obtener el usuario de la sesión
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        // Redirigir según el tipo de usuario
        if (usuario instanceof Cliente) {
            return "redirect:/cliente/home";
        } else if (usuario instanceof Administrador) {
            return "redirect:/administrador/inicio";
        } else {
            // Si no es ninguno de los tipos conocidos, redirigir a la página de bienvenida
            return "redirect:/bienvenida";
        }
    }
}