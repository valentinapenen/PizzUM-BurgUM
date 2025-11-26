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

    // Listado de productos
    @GetMapping
    public String listarProductos(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "administrador/producto/lista";
    }

    // Formulario de creación
    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "administrador/producto/form";
    }

    // Crear producto
    @PostMapping
    public String crearProducto(@Valid @ModelAttribute("producto") Producto producto) {
        // disponible = true por defecto
        producto.setDisponible(true);
        productoService.crear(producto);
        return "redirect:/admin/productos";
    }

    //Formulario de edición
    @GetMapping("/{id}/editar")
    public String editarProducto(@PathVariable long id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "administrador/producto/form";
    }

    // Actualizar precio
    @PostMapping("/{id}/actualizar-precio")
    public String actualizarPrecio(@PathVariable long id, @RequestParam double precio) {
        productoService.actualizarPrecio(id, precio);
        return "redirect:/admin/productos";
    }

    // Desactivar producto
    @PostMapping("/{id}/desactivar")
    public String desactivarProducto(@PathVariable long id) {
        productoService.desactivar(id);
        return "redirect:/admin/productos";
    }

    // Activar producto
    @PostMapping("/{id}/activar")
    public String activarProducto(@PathVariable long id) {
        productoService.activar(id);
        return "redirect:/admin/productos";
    }
}
