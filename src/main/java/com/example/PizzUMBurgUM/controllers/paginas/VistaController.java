package com.example.PizzUMBurgUM.controllers.paginas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VistaController {

    // Página de inicio
    @GetMapping("/")
    public String inicio() {
        return "inicio";
    }

    // Mostrar formulario de iniciar sesión
    @GetMapping("/iniciar-sesion")
    public String mostrarIniciarSesion() {
        return "iniciar-sesion";
    }

    // Procesar inicio de sesión
    @PostMapping("/iniciar-sesion")
    public String procesarIniciarSesion(
            @RequestParam String email,
            @RequestParam String contrasena,
            @RequestParam(required = false) String recordar,
            Model model) {

        if (email.isEmpty() || contrasena.isEmpty()) {
            model.addAttribute("error", "Por favor completa todos los campos");
            return "iniciar-sesion";
        }

        // TODO: Validar credenciales contra la base de datos

        return "redirect:/panel";
    }

    // Mostrar formulario de registro
    @GetMapping("/registrarse")
    public String mostrarRegistro() {
        return "registrarse";
    }

    // Procesar registro
    @PostMapping("/registrarse")
    public String procesarRegistro(
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String cedula,
            @RequestParam String fechaNacimiento,
            @RequestParam String telefono,
            @RequestParam String correo,
            @RequestParam String contrasena,
            @RequestParam String contrasena2,
            @RequestParam(required = false) String tyc,
            Model model) {

        if (contrasena.length() < 8) {
            model.addAttribute("error", "La contraseña debe tener al menos 8 caracteres");
            return "registrarse";
        }

        if (!contrasena.equals(contrasena2)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registrarse";
        }

        if (tyc == null) {
            model.addAttribute("error", "Debes aceptar los términos y condiciones");
            return "registrarse";
        }

        // TODO: Guardar usuario en la base de datos

        return "redirect:/iniciar-sesion?registrado=true";
    }
}