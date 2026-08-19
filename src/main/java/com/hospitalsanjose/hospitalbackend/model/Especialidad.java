package com.hospitalsanjose.hospitalbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Especialidades")
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    // NUEVO (Sprint 3): permite "desactivar" una especialidad desde el
    // panel de mantenimiento sin borrarla físicamente (evita romper FKs
    // con Medicos/Citas que ya la referencian).
    @Column(nullable = false)
    private Boolean activo = true;
}