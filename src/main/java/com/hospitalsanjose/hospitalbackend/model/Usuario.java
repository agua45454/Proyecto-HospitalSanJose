package com.hospitalsanjose.hospitalbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    // NUEVO (Sprint 3): @JsonIgnore evita que el hash de la contraseña viaje
    // en las respuestas JSON (por ejemplo dentro de cita.paciente.usuario o
    // cita.medico.usuario). No afecta el login (usa el repositorio/BD
    // directamente, no JSON) ni ninguna vista existente, ya que ningún
    // HTML de Sprint 1/2 lee este campo.
    @JsonIgnore
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private Boolean activo = true;
}