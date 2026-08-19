package com.hospitalsanjose.hospitalbackend.service;

import com.hospitalsanjose.hospitalbackend.dto.EspecialidadDTO;
import com.hospitalsanjose.hospitalbackend.model.Especialidad;
import com.hospitalsanjose.hospitalbackend.repository.EspecialidadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    @Transactional
    public Especialidad crear(EspecialidadDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new RuntimeException("El nombre de la especialidad es obligatorio.");
        }
        if (especialidadRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una especialidad con ese nombre.");
        }

        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());
        especialidad.setActivo(true);
        return especialidadRepository.save(especialidad);
    }

    @Transactional
    public Especialidad actualizar(Integer id, EspecialidadDTO dto) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));

        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());
        return especialidadRepository.save(especialidad);
    }

    @Transactional
    public Especialidad cambiarEstado(Integer id, boolean activo) {
        Especialidad especialidad = especialidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));

        especialidad.setActivo(activo);
        return especialidadRepository.save(especialidad);
    }
}
