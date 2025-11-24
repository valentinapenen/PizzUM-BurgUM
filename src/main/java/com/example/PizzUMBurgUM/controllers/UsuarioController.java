package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.LoginRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.services.AdministradorService;
import com.example.PizzUMBurgUM.services.ClienteService;
import com.example.PizzUMBurgUM.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/usuario")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ClienteService clienteService;

    // Mostrar página de login
    @GetMapping("/login")
    public String mostrarLogin(Model model){
        model.addAttribute("loginRequest", new LoginRequest());
        return "inicio/iniciar-sesion";
    }

    // Procesar login
    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, Model model, HttpSession session){
        try{
            Usuario usuario = usuarioService.login(loginRequest.getCorreo(), loginRequest.getContrasena());

            // Usuario incorrecto
            if (usuario == null) {
                model.addAttribute("error", "Correo o contraseña incorrectos.");
                return "inicio/iniciar-sesion";
            }

            // Guardar usuario en sesión
            session.setAttribute("usuarioLogueado",  usuario);

            // Redirigir al inicio correspondiente para cada tipo de usuario
            if(usuario instanceof Cliente){
                return ("redirect:/cliente/inicio-cliente");
            }

            if(usuario instanceof Administrador){
                return("redirect:/administrador/inicio-administrador");
            }

            return("redirect:/");
        }
        catch(IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());

            return "inicio/iniciar-sesion";
        }
    }

    // Logout: redirige al login
    @GetMapping("/logout")
    public String logout(HttpSession sesion){
        sesion.invalidate();

        return "redirect:/usuario/login";
    }

    // FALTA LA PAGINA DE INICIO DE CLIENTE AUN, POR ESO NO IDENTIFICA LAS RUTAS
    @GetMapping("/cuenta/cliente")
    public String mostrarCuentaCliente(Model model, HttpSession session){
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if (cliente == null) {
            return "redirect:/usuario/login";
        }
        model.addAttribute("cliente", cliente);
        return "cliente/cuenta-cliente";
    }

    @PostMapping("/cuenta/cliente")
    public String actualizarCuentaCliente(@ModelAttribute Cliente nuevosDatos, HttpSession session, Model model){
        Cliente clienteActual = (Cliente) session.getAttribute("usuarioLogueado");

        try {
            Cliente actualizado = clienteService.actualizarCliente(clienteActual.getId(), nuevosDatos);

            // Actualizar sesión:
            session.setAttribute("usuarioLogueado", actualizado);

            model.addAttribute("success", "Datos actualizados correctamente.");
            model.addAttribute("cliente", actualizado);
            return "cliente/cuenta-cliente";

        } catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cliente", clienteActual);
            return "cliente/inicio-cliente";
        }
    }
}
