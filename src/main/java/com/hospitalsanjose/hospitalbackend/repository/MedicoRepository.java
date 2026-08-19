package com.hospitalsanjose.hospitalbackend.repository;

import com.hospitalsanjose.hospitalbackend.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    boolean existsByColegiatura(String colegiatura);

    // NUEVO (Sprint 3): permite ubicar al médico autenticado (por su correo
    // de login) para la vista /historia-clinica.
    Optional<Medico> findByUsuarioCorreo(String correo);
}