package com.hospitalsanjose.hospitalbackend.repository;

import com.hospitalsanjose.hospitalbackend.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {
    List<HistoriaClinica> findByPacienteIdPacienteOrderByFechaAtencionDesc(Integer idPaciente);
    List<HistoriaClinica> findByMedicoIdMedicoOrderByFechaAtencionDesc(Integer idMedico);
}
