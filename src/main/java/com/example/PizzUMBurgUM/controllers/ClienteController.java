package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.services.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/home")
    public String paginaInicioCliente(HttpSession session, Model model){
        Usuario usuario = (Usuario)  session.getAttribute("usuarioLogueado");

        if(usuario == null || !(usuario instanceof Cliente)){
            return "redirect:/iniciar-sesion";
        }

        Cliente cliente = (Cliente) usuario;
        model.addAttribute("cliente", cliente);

        return "cliente/inicio-cliente";
    }
}
