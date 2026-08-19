package com.hospitalsanjose.hospitalbackend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "id_consultorio", nullable = false)
    private Consultorio consultorio;

    @ManyToOne
    @JoinColumn(name = "id_horario", nullable = false)
    private Horario horario;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_cita", nullable = false)
    private LocalTime horaCita;

    @Column(nullable = false, length = 20)
    private String estado = "Pendiente";

    @Column(length = 255)
    private String observaciones;
}