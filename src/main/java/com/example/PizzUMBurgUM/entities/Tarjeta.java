package com.example.PizzUMBurgUM.entities;

import com.example.PizzUMBurgUM.entities.enums.TipoTarjeta;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Tarjeta {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String numeroEnmascarado;
    @NotNull
    private String nombreTitular; // Porque el titular de la tarjeta puede no ser el mismo que quien la usa
    @ManyToOne
    @JoinColumn(name = "cliente_correo")
    @NotNull
    private Cliente cliente;
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoTarjeta tipoTarjeta;
    @NotNull
    @DateTimeFormat
    private Date fecha_vencimiento;
    @NotNull
    private boolean predeterminada;

    public static String enmascarar(String numero) {
        return "**** **** **** " + numero.substring(numero.length() - 4);
    }

    public Tarjeta(String numeroEnmascarado, String nombreTitular, Cliente cliente, TipoTarjeta tipoTarjeta, Date fechaVencimiento, boolean predeterminada) {
        this.numeroEnmascarado = numeroEnmascarado;
        this.nombreTitular = nombreTitular;
        this.cliente = cliente;
        this.tipoTarjeta = tipoTarjeta;
        this.fecha_vencimiento = fechaVencimiento;
        this.predeterminada = predeterminada;
    }
}
