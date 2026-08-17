package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.ConsultorioDTO;
import com.hospitalsanjose.hospitalbackend.model.Consultorio;
import com.hospitalsanjose.hospitalbackend.service.ConsultorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultorios")
@CrossOrigin(origins = "*")
public class ConsultorioController {

    @Autowired
    private ConsultorioService consultorioService;

    @GetMapping
    public ResponseEntity<List<Consultorio>> listar() {
        return ResponseEntity.ok(consultorioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ConsultorioDTO dto) {
        try {
            return new ResponseEntity<>(consultorioService.crear(dto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody ConsultorioDTO dto) {
        try {
            return ResponseEntity.ok(consultorioService.actualizar(id, dto));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
