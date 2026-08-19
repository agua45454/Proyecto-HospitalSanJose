package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.ReporteResumenDTO;
import com.hospitalsanjose.hospitalbackend.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @GetMapping("/resumen")
    public ResponseEntity<ReporteResumenDTO> resumen() {
        return ResponseEntity.ok(reporteService.generarResumen());
    }
}
