package com.hospitalsanjose.hospitalbackend.dto;

public class ChatResponseDTO {
    private String respuesta;

    public ChatResponseDTO(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() { return respuesta; }
    public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
}