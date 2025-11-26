package com.example.PizzUMBurgUM.controllers;

import com.example.PizzUMBurgUM.entities.Cliente;
import com.example.PizzUMBurgUM.entities.Creacion;
import com.example.PizzUMBurgUM.entities.Pedido;
import com.example.PizzUMBurgUM.entities.Usuario;
import com.example.PizzUMBurgUM.entities.enums.CategoriaProducto;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TipoCreacion;
import com.example.PizzUMBurgUM.entities.enums.TipoProducto;
import com.example.PizzUMBurgUM.services.ClienteService;
import com.example.PizzUMBurgUM.services.CreacionService;
import com.example.PizzUMBurgUM.services.ProductoService;
import com.example.PizzUMBurgUM.services.PrecioTamanoPizzaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.PizzUMBurgUM.services.PedidoService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.PizzUMBurgUM.repositories.UsuarioRepository;

@Controller

@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private CreacionService creacionService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private PrecioTamanoPizzaService precioTamanoPizzaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Obtiene el cliente desde la sesión; si no está, lo rehidrata usando
     * el usuario autenticado por Spring Security y lo guarda en sesión.
     */
    private Cliente obtenerClienteDesdeSesionOSeguridad(HttpSession session) {
        // 1) Sesión existente
        Object obj = session.getAttribute("usuarioLogueado");
        if (obj instanceof Cliente) {
            return (Cliente) obj;
        }

        // 2) Rehidratar desde Spring Security (evitar usuario anónimo)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            String correo = auth.getName();
            if (correo != null && !"anonymousUser".equalsIgnoreCase(correo)) {
                Usuario u = usuarioRepository.findByCorreo(correo);
                if (u instanceof Cliente c) {
                    // Guardar en sesión para siguientes requests
                    session.setAttribute("usuarioLogueado", c);
                    return c;
                }
            }
        }
        return null;
    }


    @GetMapping("/home")
    public String paginaInicioCliente(HttpSession session, Model model){
        // Reutilizamos la misma estrategia de rehidratación que en los POST
        Cliente cliente = obtenerClienteDesdeSesionOSeguridad(session);
        if (cliente == null) {
            return "redirect:/iniciar-sesion";
        }
        model.addAttribute("cliente", cliente);

        return "cliente/inicio-cliente";
    }

    @GetMapping("/historial")
    public String verHistorialCliente(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("historial", pedidoService.listarPorCliente(cliente.getId()));

        return "cliente/historial/lista";
    }

    @GetMapping("/pedido/{id}")
    public String verDetallePedido(@PathVariable Long id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        Pedido pedido = pedidoService.obtenerPedido(id);

        if (pedido.getCliente().getId() != cliente.getId()) {
            return "redirect:/cliente/historial";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("pedido", pedido);

        return "cliente/historial/detalle-cliente";
    }


    @GetMapping("/favoritos")
    public String verFavoritosCliente(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("favoritos", creacionService.listarFavoritosPorCliente(cliente.getId()));

        return "cliente/favoritos/lista";
    }


    @GetMapping("/perfil")
    public String verPerfilCliente(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        model.addAttribute("cliente", cliente);

        return "cliente/perfil";
    }

    // form para datos personales
    @GetMapping("/perfil/editar")
    public String mostrarFormularioEditar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente clienteSession)) {
            return "redirect:/iniciar-sesion";
        }

        // Cargar datos actuales desde BD
        Cliente clienteBD = clienteService.buscarPorId(clienteSession.getId());
        model.addAttribute("cliente", clienteBD);

        // templates/cliente/datosPersonales/form.html
        return "cliente/datosPersonales/form";
    }


    // procesar formulario para cambiar datos personales
    @PostMapping("/perfil/editar")
    public String procesarEdicion(@ModelAttribute("cliente") Cliente datosNuevos, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente clienteSession)) {
            return "redirect:/iniciar-sesion";
        }

        try {
            // Actualizar datos en BD usando el service
            Cliente actualizado = clienteService.actualizarCliente(clienteSession.getId(), datosNuevos);

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

    @GetMapping("/hacer-creacion")
    public String mostrarMenuCreaciones(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion"; // redirección consistente con seguridad
        }

        model.addAttribute("cliente", cliente);


        return "cliente/creaciones/CreacionesPrincipalMenu";
    }


    // form pizza
    @GetMapping("/creaciones/pizza/nueva")
    public String mostrarFormularioPizza(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        model.addAttribute("cliente", cliente);

        // Tamaños (enum)
        model.addAttribute("tamanos", TamanoPizza.values());

        // Precios actuales por tamaño (para mostrar y calcular el total aproximado en el cliente)
        java.util.Map<String, Double> preciosTam = new java.util.HashMap<>();
        for (TamanoPizza t : TamanoPizza.values()) {
            preciosTam.put(t.name(), precioTamanoPizzaService.obtenerPrecioPorTamano(t));
        }
        model.addAttribute("preciosTam", preciosTam);

        // Productos para pizza (tipo + categoría PIZZA o AMBOS)
        model.addAttribute("masas",
                productoService.listarPorTipoYCategoria(TipoProducto.MASA, CategoriaProducto.PIZZA));

        model.addAttribute("salsas",
                productoService.listarPorTipoYCategoria(TipoProducto.SALSA, CategoriaProducto.PIZZA));

        model.addAttribute("quesos",
                productoService.listarPorTipoYCategoria(TipoProducto.QUESO, CategoriaProducto.PIZZA));

        model.addAttribute("toppings",
                productoService.listarPorTipoYCategoria(TipoProducto.TOPPING, CategoriaProducto.PIZZA));

        model.addAttribute("bebidas",
                productoService.listarPorTipoYCategoria(TipoProducto.BEBIDA, CategoriaProducto.AMBOS));
        model.addAttribute("papas",
                productoService.listarPorTipoYCategoria(TipoProducto.PAPAS, CategoriaProducto.AMBOS));


        // va a templates/cliente/creaciones/formPizza.html
        return "cliente/creaciones/formPizza";
    }


    @PostMapping("/creaciones/pizza/guardar")
    public String guardarPizza(
            @RequestParam("tamano") TamanoPizza tamano,   // por ahora solo lo recibimos
            @RequestParam("masaId") Long masaId,
            @RequestParam("salsaId") Long salsaId,
            @RequestParam("quesoId") Long quesoId,
            @RequestParam(value = "toppingIds", required = false) java.util.List<Long> toppingIds,
            @RequestParam(value = "bebidaIds", required = false) java.util.List<Long> bebidaIds,
            @RequestParam(value = "papasIds", required = false) java.util.List<Long> papasIds,
            @RequestParam(value = "favorito", defaultValue = "false") boolean favorito,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {

        Cliente cliente = obtenerClienteDesdeSesionOSeguridad(session);
        if (cliente == null) {
            // No mandamos al login para no romper el flujo si hay un desfasaje de sesión.
            // Mostramos un mensaje y volvemos al menú de creación.
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No pudimos identificar tu sesión. Intentá nuevamente. Si el problema persiste, iniciá sesión otra vez."
            );
            return "redirect:/cliente/hacer-creacion";
        }

        // 👉 delegamos la lógica de armar la pizza al service
        Creacion creacion = creacionService.crearPizzaCompleta(
                cliente.getId(),
                tamano,
                masaId,
                salsaId,
                quesoId,
                toppingIds,
                bebidaIds,
                papasIds,
                favorito
        );

        redirectAttributes.addFlashAttribute(
                "exito",
                "Tu pizza se creó correctamente."
        );

        return "redirect:/cliente/hacer-creacion";
    }


    // form hamburguesa
    @GetMapping("/creaciones/hamburguesa/nueva")
    public String mostrarFormularioHamburguesa(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        model.addAttribute("cliente", cliente);

        // Productos para hamburguesa (tipo + categoría HAMBURGUESA o AMBOS)
        model.addAttribute("panes",
                productoService.listarPorTipoYCategoria(TipoProducto.PAN, CategoriaProducto.HAMBURGUESA));

        model.addAttribute("carnes",
                productoService.listarPorTipoYCategoria(TipoProducto.CARNE, CategoriaProducto.HAMBURGUESA));

        model.addAttribute("quesosHamb",
                productoService.listarPorTipoYCategoria(TipoProducto.QUESO, CategoriaProducto.HAMBURGUESA));

        model.addAttribute("aderezos",
                productoService.listarPorTipoYCategoria(TipoProducto.ADEREZO, CategoriaProducto.HAMBURGUESA));

        model.addAttribute("toppingsHamb",
                productoService.listarPorTipoYCategoria(TipoProducto.TOPPING, CategoriaProducto.HAMBURGUESA));

        model.addAttribute("bebidasHamb",
                productoService.listarPorTipoYCategoria(TipoProducto.BEBIDA, CategoriaProducto.AMBOS));

        model.addAttribute("papasHamb",
                productoService.listarPorTipoYCategoria(TipoProducto.PAPAS, CategoriaProducto.AMBOS));

        // va a templates/cliente/creaciones/formHamburguesa.html
        return "cliente/creaciones/formHamburguesa";
    }

    @PostMapping("/creaciones/hamburguesa/guardar")
    public String guardarHamburguesa(
            @RequestParam("panId") Long panId,
            @RequestParam("carneId") Long carneId,
            @RequestParam(value = "carneCantidad", defaultValue = "1") Integer carneCantidad,
            @RequestParam("quesoId") Long quesoId,
            @RequestParam(value = "aderezoIds", required = false) java.util.List<Long> aderezoIds,
            @RequestParam(value = "toppingIds", required = false) java.util.List<Long> toppingIds,
            @RequestParam(value = "bebidaIds", required = false) java.util.List<Long> bebidaIds,
            @RequestParam(value = "papasIds", required = false) java.util.List<Long> papasIds,
            @RequestParam(value = "favorito", defaultValue = "false") boolean favorito,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {

        Cliente cliente = obtenerClienteDesdeSesionOSeguridad(session);
        if (cliente == null) {
            return "redirect:/iniciar-sesion";
        }

        int cant = carneCantidad == null ? 1 : Math.max(1, Math.min(3, carneCantidad));

        Creacion creacion = creacionService.crearHamburguesaCompleta(
                cliente.getId(),
                panId,
                carneId,
                cant,
                quesoId,
                aderezoIds,
                toppingIds,
                bebidaIds,
                papasIds,
                favorito
        );

        redirectAttributes.addFlashAttribute(
                "exito",
                "Tu hamburguesa se creó correctamente."
        );

        return "redirect:/cliente/hacer-creacion";
    }

    @GetMapping("/carrito")
    public String verCarrito(HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        model.addAttribute("cliente", cliente);

        // creaciones en carrito
        java.util.List<Creacion> creaciones = creacionService.listarPorCliente(cliente.getId());
        model.addAttribute("creaciones", creaciones);

        double total = creaciones.stream()
                .mapToDouble(Creacion::getPrecioTotal)
                .sum();
        model.addAttribute("totalCarrito", total);

        // pedido en curso (para decidir qué botón mostrar)
        java.util.Optional<Pedido> pedidoEnCursoOpt =
                pedidoService.buscarPedidoEnCurso(cliente.getId());

        model.addAttribute("tienePedidoEnCurso", pedidoEnCursoOpt.isPresent());
        pedidoEnCursoOpt.ifPresent(p ->
                model.addAttribute("pedidoEnCurso", p)
        );


        return "cliente/carrito/lista";
    }


    //eliminar una creacion del carrito
    @PostMapping("/carrito/eliminar/{idCreacion}")
    public String eliminarCreacionDelCarrito(
            @PathVariable Long idCreacion,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        try {
            creacionService.eliminarCreacion(idCreacion);

            redirectAttributes.addFlashAttribute(
                    "exito",
                    "La creación fue eliminada del carrito."
            );

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se pudo eliminar la creación."
            );
        }

        return "redirect:/cliente/carrito";
    }

    @GetMapping("/pedido/seguimiento/{id}")
    public String verSeguimientoPedido(@PathVariable Long id,
                                       HttpSession session,
                                       Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        Pedido pedido = pedidoService.obtenerPedido(id);

        // Seguridad básica: el pedido tiene que ser del cliente logueado
        if (pedido.getCliente().getId() != cliente.getId()) {
            return "redirect:/cliente/historial";
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("pedido", pedido);


        return "cliente/carrito/botonSeguimientoPedido";
    }

    @PostMapping("/pedido/{id}/cancelar")
    public String cancelarPedido(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        try {
            pedidoService.cancelarPedido(cliente.getId(), id);
            redirectAttributes.addFlashAttribute("exito", "Tu pedido fue cancelado. Las creaciones no favoritas se eliminaron.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo cancelar el pedido: " + e.getMessage());
        }

        // Volver a la pantalla de seguimiento para ver el estado actualizado
        return "redirect:/cliente/pedido/seguimiento/" + id;
    }


    @GetMapping("/carrito/continuar")
    public String continuarCompra(HttpSession session, Model model,
                                  RedirectAttributes redirectAttributes) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        // Si el carrito está vacío → volver
        java.util.List<Creacion> creaciones = creacionService.listarPorCliente(cliente.getId());
        if (creaciones.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito está vacío.");
            return "redirect:/cliente/carrito";
        }

        // Si ya tiene pedido en curso → no dejar continuar
        java.util.Optional<Pedido> pedidoEnCursoOpt =
                pedidoService.buscarPedidoEnCurso(cliente.getId());
        if (pedidoEnCursoOpt.isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "Ya tenés un pedido en curso. Podés ver el seguimiento desde el carrito."
            );
            return "redirect:/cliente/carrito";
        }

        // Mandamos datos al form
        model.addAttribute("cliente", cliente);
        model.addAttribute("creaciones", creaciones);

        double total = creaciones.stream()
                .mapToDouble(Creacion::getPrecioTotal)
                .sum();
        model.addAttribute("totalCarrito", total);

        // domicilios y tarjetas del cliente
        model.addAttribute("domicilios", cliente.getDomicilios());
        model.addAttribute("tarjetas", cliente.getTarjetas());

        // va a templates/cliente/carrito/botonConfirmarPedido.html
        return "cliente/carrito/botonConfirmarPedido";
    }


    @PostMapping("/carrito/confirmar")
    public String confirmarPedido(
            @RequestParam("domicilioId") Long domicilioId,
            @RequestParam("tarjetaId") Long tarjetaId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null || !(usuario instanceof Cliente cliente)) {
            return "redirect:/iniciar-sesion";
        }

        try {
            // crear pedido desde carrito
            Pedido pedido = creacionService.crearPedidoDesdeCarrito(
                    cliente,
                    domicilioId,
                    tarjetaId
            );

            redirectAttributes.addFlashAttribute(
                    "exito",
                    "Tu pedido fue creado correctamente. Ahora está en cola."
            );

            // redirigimos al seguimiento de ese pedido
            return "redirect:/cliente/pedido/seguimiento/" + pedido.getIdPedido();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se pudo confirmar el pedido: " + e.getMessage()
            );
            // Redirigimos al paso de continuar para rehidratar el modelo (domicilios, tarjetas, creaciones, total)
            return "redirect:/cliente/carrito/continuar";
        }
    }


}
