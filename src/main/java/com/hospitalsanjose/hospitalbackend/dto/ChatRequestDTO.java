package com.hospitalsanjose.hospitalbackend.dto;

public class ChatRequestDTO {
    private String mensaje;

    public ChatRequestDTO() {}

    public ChatRequestDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}