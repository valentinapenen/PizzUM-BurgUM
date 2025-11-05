package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.RegistroClienteRequest;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Domicilio;
import com.example.PizzUMBurgUM.entities.Tarjeta;
import com.example.PizzUMBurgUM.services.ClienteServicio;
import com.example.PizzUMBurgUM.services.UsuarioServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cliente")
@SessionAttributes("usuarioLogueado")
public class ClienteController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private ClienteServicio clienteServicio;

    @GetMapping("/home")
    public String paginaInicioCliente(@SessionAttribute("usuarioLogueado") Cliente cliente, Model model){
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
            clienteServicio.regisatrarCliente(registroRequest);
            redirectAttributes.addFlashAttribute("exito","Cuenta creada exitosamente, ahora puede iniciar sesión.");
            return "redirect:/login";


        }
        catch (IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());
            return "registrarse";
        }
    }
}
