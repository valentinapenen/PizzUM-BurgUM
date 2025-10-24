package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Producto;
import com.example.PizzUMBurgUM.services.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/productos")
@PreAuthorize("hasRole('ADMIN')")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody @Valid Producto producto) {
        return ResponseEntity.ok(productoService.crear(producto));
    }

    @PutMapping("/{id}/precio")
    public ResponseEntity<Producto> actualizarPrecio(@PathVariable long id, @RequestParam double precio) {
        return ResponseEntity.ok(productoService.actualizarPrecio(id, precio));
    }
}
