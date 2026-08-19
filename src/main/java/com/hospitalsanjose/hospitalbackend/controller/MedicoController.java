package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.EstadoDTO;
import com.hospitalsanjose.hospitalbackend.model.Medico;
import com.hospitalsanjose.hospitalbackend.repository.MedicoRepository;
import com.hospitalsanjose.hospitalbackend.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/medicos")
@CrossOrigin(origins = "*")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    // NUEVO (Sprint 3): para el mantenimiento de médicos en el panel admin.
    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public ResponseEntity<?> listarMedicos() {
        try {
            List<Medico> medicos = medicoRepository.findAll();
            
            List<Map<String, Object>> respuesta = medicos.stream().map(medico -> {
                Map<String, Object> map = new HashMap<>();
                map.put("idMedico", medico.getIdMedico());
                map.put("nombres", medico.getNombres());
                map.put("apellidos", medico.getApellidos());
                
                if (medico.getEspecialidad() != null) {
                    Map<String, Object> espMap = new HashMap<>();
                    espMap.put("nombre", medico.getEspecialidad().getNombre());
                    map.put("especialidad", espMap);
                } else {
                    map.put("especialidad", null);
                }

                // NUEVO (Sprint 3): campos adicionales para el panel de
                // mantenimiento. Son claves NUEVAS en el mapa, no tocan
                // "idMedico", "nombres", "apellidos" ni "especialidad" que
                // ya usa dashboard.html (Sprint 2), por lo que ese flujo
                // sigue funcionando exactamente igual.
                map.put("colegiatura", medico.getColegiatura());
                map.put("telefono", medico.getTelefono());
                map.put("correo", medico.getUsuario() != null ? medico.getUsuario().getCorreo() : null);
                map.put("activo", medico.getUsuario() != null ? medico.getUsuario().getActivo() : null);

                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    // NUEVO (Sprint 3): activar/desactivar médico desde el panel admin
    // (no se borra físicamente para no romper Citas/HistoriaClinica ya
    // asociadas a ese médico).
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestBody EstadoDTO dto) {
        try {
            medicoService.cambiarEstado(id, Boolean.TRUE.equals(dto.getActivo()));
            return ResponseEntity.ok("Estado del médico actualizado correctamente.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}