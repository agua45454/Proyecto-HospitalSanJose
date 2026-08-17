package com.hospitalsanjose.hospitalbackend.repository;

import com.hospitalsanjose.hospitalbackend.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    Optional<Especialidad> findByNombre(String nombre);

    // NUEVO (Sprint 3): validación de nombre único en el panel de mantenimiento.
    boolean existsByNombre(String nombre);
}