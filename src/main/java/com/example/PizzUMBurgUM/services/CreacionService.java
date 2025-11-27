package com.example.PizzUMBurgUM.services;

import com.example.PizzUMBurgUM.entities.*;
import com.example.PizzUMBurgUM.entities.enums.EstadoPedido;
import com.example.PizzUMBurgUM.entities.enums.MedioDePago;
import com.example.PizzUMBurgUM.entities.enums.TamanoPizza;
import com.example.PizzUMBurgUM.entities.enums.TipoCreacion;
import com.example.PizzUMBurgUM.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreacionService {

    @Autowired
    private CreacionRepository creacionRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private DomicilioRepository domicilioRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private PrecioTamanoPizzaService precioTamanoPizzaService;


    // Hacer creación
    @Transactional
    public Creacion crearCreacion(long clienteId, TipoCreacion tipo, List<Long> idsProductos, boolean favorito) {
        // Regla: no permitir agregar creaciones al carrito si el cliente tiene un pedido en curso
        validarSinPedidoEnCurso(clienteId);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Producto> productos = productoRepository.findAllById(idsProductos);
        double total = productos.stream().mapToDouble(Producto::getPrecio).sum();

        Creacion creacion = Creacion.builder()
                .cliente(cliente)
                .tipo(tipo)
                .productos(productos)
                .favorito(favorito)
                .enCarrito(true)
                .precioTotal(total)
                .build();

        return creacionRepository.save(creacion);
    }


    // Listar todas las creaciones de un cliente
    public List<Creacion> listarPorCliente(Long clienteId) {
        // Mostrar sólo las creaciones que están marcadas como enCarrito
        return creacionRepository.findByClienteIdAndEnCarritoTrue(clienteId);
    }


    public List<Creacion> listarFavoritosPorCliente(Long clienteId) {
        return creacionRepository.findByClienteIdAndFavoritoTrue(clienteId);
    }


    /**
     * Agrega al carrito una NUEVA creación basada en una favorita del cliente.
     * No modifica la favorita original; permite agregarla múltiples veces.
     */
    @Transactional
    public Creacion agregarFavoritoAlCarrito(Long creacionId, Long clienteId) {
        // Regla: no permitir agregar creaciones al carrito si hay pedido en curso
        validarSinPedidoEnCurso(clienteId);
        Creacion favorita = creacionRepository.findById(creacionId)
                .orElseThrow(() -> new EntityNotFoundException("Creación no encontrada con ID: " + creacionId));

        if (favorita.getCliente() == null || favorita.getCliente().getId() != clienteId) {
            throw new IllegalArgumentException("La creación no pertenece al cliente");
        }

        if (!favorita.isFavorito()) {
            throw new IllegalStateException("La creación no está marcada como favorita");
        }

        // Clonar la favorita en una nueva creación para el carrito
        // IMPORTANTE: crear una NUEVA lista para evitar compartir la misma
        // colección de Hibernate (PersistentBag) entre entidades distintas,
        // lo cual genera el error "Found shared references to a collection".
        java.util.List<Producto> productosClonados = new java.util.ArrayList<>(favorita.getProductos());
        Creacion nueva = Creacion.builder()
                .cliente(favorita.getCliente())
                .tipo(favorita.getTipo())
                .productos(productosClonados)
                .tamanoPizza(favorita.getTamanoPizza())
                .precioTotal(favorita.getPrecioTotal())
                .favorito(false)
                .enCarrito(true)
                .build();

        return creacionRepository.save(nueva);
    }


    // Marcar o desmarcar como favorita
    public Creacion marcarFavorito(Long creacionId, boolean favorito) {
        Creacion creacion = creacionRepository.findById(creacionId)
                .orElseThrow(() -> new EntityNotFoundException("Creación no encontrada con ID: " + creacionId));
        creacion.setFavorito(favorito);
        return creacionRepository.save(creacion);
    }



    // Eliminar una creación
    public void eliminarCreacion(Long creacionId) {
        Creacion c = creacionRepository.findById(creacionId)
                .orElseThrow(() -> new EntityNotFoundException("Creación no encontrada con ID: " + creacionId));
        if (c.isFavorito()) {
            // Si es favorita, no la borramos; sólo la quitamos del carrito
            c.setEnCarrito(false);
            creacionRepository.save(c);
        } else {
            creacionRepository.delete(c);
        }
    }


    /**
     * Duplica una creación que está en el carrito para simular "sumar" una unidad.
     * Crea una nueva Creacion con los mismos datos y una NUEVA lista de productos
     * para evitar compartir la colección de Hibernate entre entidades (PersistentBag).
     */
    @Transactional
    public Creacion duplicarCreacionEnCarrito(Long creacionId, Long clienteId) {
        // Regla: no permitir agregar más unidades si hay pedido en curso
        validarSinPedidoEnCurso(clienteId);
        Creacion original = creacionRepository.findById(creacionId)
                .orElseThrow(() -> new EntityNotFoundException("Creación no encontrada con ID: " + creacionId));

        if (original.getCliente() == null || original.getCliente().getId() != clienteId) {
            throw new IllegalArgumentException("La creación no pertenece al cliente");
        }

        if (!original.isEnCarrito()) {
            throw new IllegalStateException("La creación no está en el carrito");
        }

        java.util.List<Producto> productosClonados = new java.util.ArrayList<>(original.getProductos());
        Creacion copia = Creacion.builder()
                .cliente(original.getCliente())
                .tipo(original.getTipo())
                .productos(productosClonados)
                .tamanoPizza(original.getTamanoPizza())
                .precioTotal(original.getPrecioTotal())
                // la copia no se marca como favorita aunque la original lo sea; es un item del carrito
                .favorito(false)
                .enCarrito(true)
                .build();

        return creacionRepository.save(copia);
    }

    /**
     * Lanza IllegalStateException si el cliente tiene un pedido en curso
     * (EN_COLA, EN_PREPARACION o EN_CAMINO).
     */
    private void validarSinPedidoEnCurso(Long clienteId) {
        java.util.List<com.example.PizzUMBurgUM.entities.enums.EstadoPedido> estadosEnCurso = java.util.List.of(
                com.example.PizzUMBurgUM.entities.enums.EstadoPedido.EN_COLA,
                com.example.PizzUMBurgUM.entities.enums.EstadoPedido.EN_PREPARACION,
                com.example.PizzUMBurgUM.entities.enums.EstadoPedido.EN_CAMINO
        );

        java.util.Optional<com.example.PizzUMBurgUM.entities.Pedido> pedidoOpt =
                pedidoRepository.findFirstByClienteIdAndEstadoInOrderByFechaDesc(clienteId, estadosEnCurso);

        if (pedidoOpt.isPresent()) {
            throw new IllegalStateException("Ya tenés un pedido en curso. No podés agregar más creaciones al carrito.");
        }
    }


    @Transactional
    public Creacion crearPizzaCompleta(Long clienteId,
                                       TamanoPizza tamano,
                                       Long masaId,
                                       Long salsaId,
                                       Long quesoId,
                                       java.util.List<Long> toppingIds,
                                       java.util.List<Long> bebidaIds,
                                       java.util.List<Long> papasIds,
                                       boolean favorito) {

        java.util.List<Long> productosIds = new java.util.ArrayList<>();
        productosIds.add(masaId);
        productosIds.add(salsaId);
        productosIds.add(quesoId);

        if (toppingIds != null) {
            productosIds.addAll(toppingIds);
        }
        if (bebidaIds != null) {
            productosIds.addAll(bebidaIds);
        }
        if (papasIds != null) {
            productosIds.addAll(papasIds);
        }

        // Reutilizamos la lógica genérica para crear la base
        Creacion creacion = crearCreacion(clienteId, TipoCreacion.PIZZA, productosIds, favorito);

        // Sumar precio base por tamaño y persistir el tamaño elegido
        double precioBaseTamano = precioTamanoPizzaService.obtenerPrecioPorTamano(tamano);
        creacion.setTamanoPizza(tamano);
        creacion.setPrecioTotal(creacion.getPrecioTotal() + precioBaseTamano);
        return creacionRepository.save(creacion);
    }

    @Transactional
    public Creacion crearHamburguesaCompleta(Long clienteId,
                                             Long panId,
                                             Long carneId,
                                             int carneCantidad,
                                             Long quesoId,
                                             java.util.List<Long> aderezoIds,
                                             java.util.List<Long> toppingIds,
                                             java.util.List<Long> bebidaIds,
                                             java.util.List<Long> papasIds,
                                             boolean favorito) {

        java.util.List<Long> productosIds = new java.util.ArrayList<>();
        productosIds.add(panId);
        // Validar cantidad de carne (1..3)
        int cant = Math.max(1, Math.min(3, carneCantidad));
        // Agregamos una sola vez el ID de carne a la lista de productos
        // porque JPA findAllById usa IN y devolverá una fila por ID, no por repetición.
        // Luego ajustamos el precio total manualmente por las carnes extra.
        productosIds.add(carneId);
        productosIds.add(quesoId);

        if (aderezoIds != null) {
            productosIds.addAll(aderezoIds);
        }
        if (toppingIds != null) {
            productosIds.addAll(toppingIds);
        }
        if (bebidaIds != null) {
            productosIds.addAll(bebidaIds);
        }
        if (papasIds != null) {
            productosIds.addAll(papasIds);
        }

        // Crear la base (sumará precios de pan, 1x carne, queso y extras)
        Creacion creacion = crearCreacion(clienteId, TipoCreacion.HAMBURGUESA, productosIds, favorito);

        // Si eligió más de 1 carne, sumar las carnes adicionales al precio total
        if (cant > 1) {
            Producto carne = productoRepository.findById(carneId)
                    .orElseThrow(() -> new RuntimeException("Carne no encontrada"));
            double extra = (cant - 1) * carne.getPrecio();
            creacion.setPrecioTotal(creacion.getPrecioTotal() + extra);
            return creacionRepository.save(creacion);
        }

        return creacion;
    }

    @Transactional
    public void vaciarCarritoCliente(Long clienteId) {
        List<Creacion> creaciones = creacionRepository.findByClienteIdAndEnCarritoTrue(clienteId);
        for (Creacion c : creaciones) {
            c.setEnCarrito(false);
        }
        creacionRepository.saveAll(creaciones);
    }


    @Transactional
    public Pedido crearPedidoDesdeCarrito(Cliente cliente,
                                          Long domicilioId,
                                          Long tarjetaId) {

        // 1) Ver creaciones del cliente que están en carrito
        List<Creacion> creaciones = creacionRepository.findByClienteIdAndEnCarritoTrue(cliente.getId());
        if (creaciones.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        // 2) Buscar domicilio
        Domicilio domicilio = domicilioRepository.findById(domicilioId)
                .orElseThrow(() -> new RuntimeException("Domicilio no encontrado"));

        // 3) Buscar tarjeta
        Tarjeta tarjeta = tarjetaRepository.findById(tarjetaId)
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        // 4) Calcular total
        double total = creaciones.stream()
                .mapToDouble(Creacion::getPrecioTotal)
                .sum();

        // 5) Crear pedido
        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .creaciones(creaciones)
                .domicilio(domicilio)
                .medioDePago(MedioDePago.TARJETA)
                .estado(EstadoPedido.EN_COLA)
                .fecha(LocalDateTime.now())
                .total(total)
                .build();

        // 6) Guardar pedido
        Pedido guardado = pedidoRepository.save(pedido);

        // 7) Vaciar carrito del cliente sin borrar favoritos: marcar enCarrito = false
        for (Creacion c : creaciones) {
            c.setEnCarrito(false);
        }
        creacionRepository.saveAll(creaciones);

        return guardado;
    }








}
