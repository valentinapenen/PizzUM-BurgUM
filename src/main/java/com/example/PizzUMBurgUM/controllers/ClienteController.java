package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.services.ClienteService;
import com.example.PizzUMBurgUM.services.CreacionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.PizzUMBurgUM.services.PedidoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private CreacionService creacionService;

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

    @GetMapping("/historial")
    public String verHistorialCliente(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }

        model.addAttribute("cliente", cliente);

        model.addAttribute("historial", pedidoService.listarPorCliente(cliente.getId()));


        return "historial/lista";
    }

    @GetMapping("/pedido/{id}")
    public String verDetallePedido(@PathVariable Long id,
                                   HttpSession session,
                                   Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");


        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }


        Pedido pedido = pedidoService.obtenerPedido(id);


        if (pedido.getCliente().getId() != cliente.getId()) {
            return "redirect:/cliente/historial";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("pedido", pedido);

        return "historial/detalle-cliente";
    }

    @GetMapping("/favoritos")
    public String verFavoritosCliente(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("favoritos", creacionService.listarFavoritosPorCliente(cliente.getId()));

        return "favoritos/lista";
    }

    @GetMapping("/perfil")
    public String verPerfilCliente(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/usuario/login";
        }


        model.addAttribute("cliente", cliente);

        return "cliente/perfil";
    }


    // form para datos personales
    @GetMapping("/perfil/editar")
    public String mostrarFormularioEditar(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente clienteSession)) {
            return "redirect:/usuario/login";
        }

        // Cargar datos actuales desde BD
        Cliente clienteBD = clienteService.buscarPorId(clienteSession.getId());

        model.addAttribute("cliente", clienteBD);

        // templates/cliente/datosPersonales/form.html
        return "cliente/datosPersonales/form";
    }


    // procesar formulario para cambiar datos personales
    @PostMapping("/perfil/editar")
    public String procesarEdicion(
            @ModelAttribute("cliente") Cliente datosNuevos,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente clienteSession)) {
            return "redirect:/usuario/login";
        }

        try {
            // Actualizar datos en BD usando el service
            Cliente actualizado = clienteService.actualizarCliente(
                    clienteSession.getId(),
                    datosNuevos
            );

            //refrescar el usuario en sesión
            session.setAttribute("usuarioLogueado", actualizado);

            redirectAttributes.addFlashAttribute(
                    "exito",
                    "Datos personales actualizados correctamente."
            );

            return "redirect:/cliente/perfil";

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cliente/perfil/editar";
        }
    }



}
