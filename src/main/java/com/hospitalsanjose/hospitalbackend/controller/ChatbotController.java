package com.hospitalsanjose.hospitalbackend.controller;

import com.hospitalsanjose.hospitalbackend.dto.ChatRequestDTO;
import com.hospitalsanjose.hospitalbackend.dto.ChatResponseDTO;
import com.hospitalsanjose.hospitalbackend.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "*")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/mensaje")
    public ResponseEntity<ChatResponseDTO> recibirMensaje(@RequestBody ChatRequestDTO request) {
        String respuesta = chatbotService.obtenerRespuestaTriaje(request.getMensaje());
        return ResponseEntity.ok(new ChatResponseDTO(respuesta));
    }
}