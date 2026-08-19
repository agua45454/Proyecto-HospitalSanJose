package com.hospitalsanjose.hospitalbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Consultorios")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consultorio")
    private Integer idConsultorio;

    @Column(nullable = false, unique = true, length = 10)
    private String numero;

    @Column(length = 10)
    private String piso;

    @Column(length = 100)
    private String ubicacion;

    @Column(nullable = false, length = 20)
    private String estado = "Disponible";
}