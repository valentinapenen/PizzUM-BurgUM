package com.example.PizzUMBurgUM.configuracion;

import com.example.PizzUMBurgUM.controllers.DTOS.CreacionAdministradorRequest;
import com.example.PizzUMBurgUM.controllers.DTOS.DomicilioRequest;
import com.example.PizzUMBurgUM.entities.Administrador;
import com.example.PizzUMBurgUM.services.AdministradorService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Inicializador opcional para crear un administrador directamente en la base de datos
 * sin necesidad del frontend. Usa JPA (herencia JOINED) por lo que primero inserta en
 * la tabla base "usuario" y luego en la tabla "administrador" automáticamente.
 *
 * Cómo activarlo:
 * 1) En application.properties agregue: app.seed-admin=true
 * 2) Opcionalmente ajuste los datos con propiedades (ver campos @Value) o edite abajo.
 * 3) Inicie la aplicación. Si el correo ya existe, no duplicará el registro.
 */
@Component
public class AdminSeeder implements CommandLineRunner {

    private final AdministradorService administradorService;

    @Value("${app.seed-admin:false}")
    private boolean seedEnabled;

    // Datos por defecto (pueden sobrescribirse vía properties)
    @Value("${app.seed-admin.nombre:Admin}")
    private String nombre;

    @Value("${app.seed-admin.apellido:Sistema}")
    private String apellido;

    @Value("${app.seed-admin.cedula:12345678}")
    private String cedula;

    @Value("${app.seed-admin.fecha-nacimiento:1990-01-01}")
    private String fechaNacimientoStr; // ISO-8601 yyyy-MM-dd

    @Value("${app.seed-admin.correo:admin@pizzum.com}")
    private String correo;

    @Value("${app.seed-admin.telefono:099000000}")
    private String telefono;

    @Value("${app.seed-admin.contrasena:admin123}")
    private String contrasena;

    // Domicilio
    @Value("${app.seed-admin.domicilio.numero:123}")
    private String domNumero;

    @Value("${app.seed-admin.domicilio.calle:Av. Principal}")
    private String domCalle;

    @Value("${app.seed-admin.domicilio.departamento:Montevideo}")
    private String domDepartamento;

    @Value("${app.seed-admin.domicilio.ciudad:Montevideo}")
    private String domCiudad;

    @Value("${app.seed-admin.domicilio.apartamento:APT 1}")
    private String domApartamento;

    public AdminSeeder(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @Override
    public void run(String... args) {
        if (!seedEnabled) return; // no correr si no está habilitado

        // Evitar duplicados por correo
        Administrador existente = administradorService.encontrarAdminPorCorreo(correo);
        if (existente != null) {
            System.out.println("[AdminSeeder] Admin ya existe, no se crea: " + correo);
            return;
        }

        try {
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoStr);

            DomicilioRequest domicilioReq = new DomicilioRequest();
            domicilioReq.setNumero(domNumero);
            domicilioReq.setCalle(domCalle);
            domicilioReq.setDepartamento(domDepartamento);
            domicilioReq.setCiudad(domCiudad);
            domicilioReq.setApartamento(domApartamento);
            domicilioReq.setPredeterminado(true);

            CreacionAdministradorRequest req = new CreacionAdministradorRequest();
            req.setNombre(nombre);
            req.setApellido(apellido);
            req.setCedula(cedula);
            req.setFechaNacimiento(fechaNacimiento);
            req.setCorreo(correo);
            req.setTelefono(telefono);
            req.setContrasena(contrasena);
            req.setDomicilio_facturacion(domicilioReq);

            Administrador creado = administradorService.crearAdministrador(req);
            System.out.println("[AdminSeeder] Administrador creado con ID=" + creado.getId() + " correo=" + correo);

        } catch (Exception e) {
            System.err.println("[AdminSeeder] Error creando administrador de semilla: " + e.getMessage());
        }
    }
}
