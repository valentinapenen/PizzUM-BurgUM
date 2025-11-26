package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/admin/ventas")
public class AdminVentasController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public String listarVentas(
            HttpSession session,
            @RequestParam(value = "desde", required = false) LocalDate desde,
            @RequestParam(value = "hasta", required = false) LocalDate hasta,
            Model model
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Administrador)) {
            return "redirect:/iniciar-sesion";
        }

        // Rango por defecto: últimos 30 días
        LocalDate hoy = LocalDate.now();
        if (hasta == null) hasta = hoy;
        if (desde == null) desde = hasta.minusDays(30);

        LocalDateTime dtDesde = desde.atStartOfDay();
        LocalDateTime dtHasta = hasta.atTime(LocalTime.MAX);

        List<Pedido> pedidos = pedidoService.listarPedidosPorFecha(dtDesde, dtHasta);
        double total = pedidos.stream().mapToDouble(Pedido::getTotal).sum();

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        model.addAttribute("total", total);

        return "administrador/ventas/lista";
    }
}
