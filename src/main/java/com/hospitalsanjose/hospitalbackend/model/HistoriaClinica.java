package com.hospitalsanjose.hospitalbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "HistoriaClinica")
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia")
    private Integer idHistoria;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    // Opcional: si la atención proviene de una cita ya reservada.
    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = true)
    private Cita cita;

    @Column(name = "fecha_atencion", nullable = false)
    private LocalDateTime fechaAtencion = LocalDateTime.now();

    @Column(name = "motivo_consulta", length = 255)
    private String motivoConsulta;

    @Column(nullable = false, length = 500)
    private String diagnostico;

    @Column(length = 500)
    private String tratamiento;

    @Column(length = 500)
    private String observaciones;
}
