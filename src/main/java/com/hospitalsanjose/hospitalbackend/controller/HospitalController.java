package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.RegistroPacienteDTO;
import com.hospitalsanjose.hospitalbackend.repository.MedicoRepository;
import com.hospitalsanjose.hospitalbackend.repository.PacienteRepository;
import com.hospitalsanjose.hospitalbackend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HospitalController {

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private PacienteRepository pacienteRepository;

    // NUEVO (Sprint 3): para resolver el médico autenticado en /historia-clinica.
    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String correo = authentication.getName();
            pacienteRepository.findByUsuarioCorreo(correo).ifPresent(p -> {
                model.addAttribute("nombrePaciente", p.getNombres() + " " + p.getApellidos());
                // CORRECCIÓN (Sprint 3): antes el dashboard.html tenía el ID
                // de paciente fijo en 1 (siempre "Juan Perez"), por lo que
                // CUALQUIER paciente que reservaba una cita, esta se
                // guardaba como si fuera de Juan. Ahora se pasa el ID real
                // del paciente autenticado.
                model.addAttribute("idPaciente", p.getIdPaciente());
            });
        }
        return "dashboard";
    }

    // NUEVO (Sprint 3): panel del médico (Historia Clínica Digital).
    @GetMapping("/historia-clinica")
    public String historiaClinica(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            String correo = authentication.getName();
            medicoRepository.findByUsuarioCorreo(correo).ifPresent(m -> {
                model.addAttribute("idMedico", m.getIdMedico());
                model.addAttribute("nombreMedico", m.getNombres() + " " + m.getApellidos());
                model.addAttribute("especialidadMedico",
                        m.getEspecialidad() != null ? m.getEspecialidad().getNombre() : "Sin especialidad asignada");
            });
        }
        return "historia-clinica";
    }

    // NUEVO (Sprint 3): panel de Administrador (Mantenimiento y Reportes).
    @GetMapping("/admin")
    public String admin(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("nombreAdmin", authentication.getName());
        }
        return "admin";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute RegistroPacienteDTO dto, RedirectAttributes redirect) {
        try {
            pacienteService.registrarPaciente(dto);
            redirect.addFlashAttribute("mensajeExito", "¡Registro exitoso! Ya puedes iniciar sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            redirect.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/registro";
        }
    }
}