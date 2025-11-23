package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.controllers.DTOS.LoginRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Usuario;
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

    @GetMapping("/login")
    public String mostrarLogin(Model model){
        model.addAttribute("loginRequest", new LoginRequest());

        return "inicio/iniciar-sesion";
    }

    @PostMapping("/login")
    public String procesarLogin(@Valid @ModelAttribute("loginRequest") LoginRequest loginRequest, Model model, HttpSession session){
        try{
            Usuario usuario = usuarioService.login(loginRequest.getCorreo(), loginRequest.getPassword());
            session.setAttribute("usuarioLogueado",  usuario);

            if(usuario instanceof Cliente){
                return ("redirect:/cliente/home");
            }

            if(usuario instanceof Administrador){
                return("redirect:/administrador/home");
            }

            return("redirect:/");
        }
        catch(IllegalArgumentException e){
            model.addAttribute("error", e.getMessage());

            return "inicio/iniciar-sesion";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession sesion){
        sesion.invalidate();

        return "redirect:/inicio";
    }
}
