package com.hospitalsanjose.hospitalbackend.repository;

import com.hospitalsanjose.hospitalbackend.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    boolean existsByDni(String dni);
}