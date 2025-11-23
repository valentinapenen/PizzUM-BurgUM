package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bps")
public class BpsRestController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/funcionarios")
    public Map<String, Long> getCantidadFuncionarios() {

        long cantidadAdmins = usuarioService.contarAdministradores();
        long cantidadClientes = usuarioService.contarClientes();
        long cantidadTotal = usuarioService.contarUsuarios();

        Map<String, Long> respuesta = new HashMap<>();
        respuesta.put("cantidadTotal", cantidadTotal);
        respuesta.put("cantidadAdministradores", cantidadAdmins);
        respuesta.put("cantidadClientes", cantidadClientes);

        return respuesta; // Spring lo serializa a JSON
    }
}

//http://localhost:8080/api/bps/funcionarios  (se entra con eso en el navegador)
