package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.services.ClienteService;
import com.example.PizzUMBurgUM.services.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/home")
    public String paginaInicioCliente(HttpSession session, Model model){

        Usuario usuario = (Usuario)  session.getAttribute("usuarioLogueado");

        if(usuario == null || !(usuario instanceof Cliente)){
            return "redirect:/usuario/login";
        }
        Cliente cliente = (Cliente) usuario;
        model.addAttribute("cliente", cliente);

        return "cliente/inicio-cliente";
    }

    @GetMapping("/registro")
    public String mostrarRegistroCliente(Model model){
        model.addAttribute("registroCliente", new RegistroClienteRequest());

        return "inicio/crear-cuenta";
    }

    @PostMapping("/registro")
    public String procesarRegistroCliente(@Valid @ModelAttribute("registroCliente") RegistroClienteRequest registroRequest, Model model, RedirectAttributes redirectAttributes){
        try{
            clienteService.registrarCliente(registroRequest);
            redirectAttributes.addFlashAttribute("exito","Cuenta creada exitosamente, ahora puede iniciar sesión.");
            return "redirect:/usuario/login";
        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "inicio/crear-cuenta";
        }
    }
}
