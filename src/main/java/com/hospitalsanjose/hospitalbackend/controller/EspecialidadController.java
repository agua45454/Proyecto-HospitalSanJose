package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.EspecialidadDTO;
import com.hospitalsanjose.hospitalbackend.dto.EstadoDTO;
import com.hospitalsanjose.hospitalbackend.model.Especialidad;
import com.hospitalsanjose.hospitalbackend.service.EspecialidadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@CrossOrigin(origins = "*")
public class EspecialidadController {

    @Autowired
    private EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<Especialidad>> listar() {
        return ResponseEntity.ok(especialidadService.listarTodas());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody EspecialidadDTO dto) {
        try {
            return new ResponseEntity<>(especialidadService.crear(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody EspecialidadDTO dto) {
        try {
            return ResponseEntity.ok(especialidadService.actualizar(id, dto));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestBody EstadoDTO dto) {
        try {
            return ResponseEntity.ok(especialidadService.cambiarEstado(id, Boolean.TRUE.equals(dto.getActivo())));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}