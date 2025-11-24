package com.example.PizzUMBurgUM.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @GetMapping("/favicon.ico")
    @ResponseBody
    void favicon() {}
}