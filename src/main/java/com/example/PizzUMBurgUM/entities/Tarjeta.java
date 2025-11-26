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
    private String numero;

    @NotNull
    private String nombreTitular; // Porque el titular de la tarjeta puede no ser el mismo que quien la usa

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @NotNull
    private Cliente cliente;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoTarjeta tipoTarjeta;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date fecha_vencimiento;

    @NotNull
    private boolean predeterminada;
}
