package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
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
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogueado");
        if(cliente == null){
            return "redirect:/usuario/login";
        }
        model.addAttribute("cliente", cliente);

        return "paginaPrincipalCliente";
    }

    @GetMapping("/registro")
    public String mostrarRegistroCliente(Model model){
        model.addAttribute("registroCliente", new RegistroClienteRequest());

        return "registrarse";
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
            return "registrarse";
        }
    }
}
