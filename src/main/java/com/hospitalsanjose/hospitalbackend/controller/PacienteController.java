package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.EstadoDTO;
import com.hospitalsanjose.hospitalbackend.model.Paciente;
import com.hospitalsanjose.hospitalbackend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<?> listarPacientes() {
        try {
            List<Paciente> pacientes = pacienteService.listarTodos();

            List<Map<String, Object>> respuesta = pacientes.stream().map(p -> {
                Map<String, Object> map = new HashMap<>();
                map.put("idPaciente", p.getIdPaciente());
                map.put("nombres", p.getNombres());
                map.put("apellidos", p.getApellidos());
                map.put("dni", p.getDni());
                map.put("telefono", p.getTelefono());
                map.put("direccion", p.getDireccion());
                map.put("correo", p.getUsuario() != null ? p.getUsuario().getCorreo() : null);
                map.put("activo", p.getUsuario() != null ? p.getUsuario().getActivo() : null);
                return map;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestBody EstadoDTO dto) {
        try {
            pacienteService.cambiarEstado(id, Boolean.TRUE.equals(dto.getActivo()));
            return ResponseEntity.ok("Estado del paciente actualizado correctamente.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
