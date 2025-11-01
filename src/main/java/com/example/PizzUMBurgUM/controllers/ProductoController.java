package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Producto;
import com.example.PizzUMBurgUM.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/productos")
@PreAuthorize("hasRole('ADMIN')")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public String listarActivos(Model model) {
        List<Producto> productos = productoService.listarActivos();
        model.addAttribute("productos", productos);
        return "productos/lista"; // → templates/productos/lista.html
    }


    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("producto", new Producto());
        return "productos/formulario"; // → templates/productos/formulario.html
    }

    @PostMapping
    public String crear(@ModelAttribute("producto") @Valid Producto producto) {
        productoService.crear(producto);
        return "redirect:/admin/productos"; // redirige a la lista después de guardar
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable long id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "productos/editar"; // → templates/productos/editar.html
    }

    @PostMapping("/{id}/actualizar-precio")
    public String actualizarPrecio(@PathVariable long id, @RequestParam double precio) {
        productoService.actualizarPrecio(id, precio);
        return "redirect:/admin/productos";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable long id) {
        productoService.desactivar(id);
        return "redirect:/admin/productos";
    }
}
