package com.hospitalsanjose.hospitalbackend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.Normalizer;
import java.util.*;

@Service
public class ChatbotService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String obtenerRespuestaTriaje(String mensajeUsuario) {
        try {
            String url = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String promptSistema = "Eres un asistente virtual de triaje médico para el Hospital San José. "
                    + "Atiende al paciente con amabilidad, orienta sus síntomas de forma preliminar y sugiérele la especialidad médica adecuada para reservar su cita. "
                    + "Mensaje del paciente: " + mensajeUsuario;

            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", promptSistema);

            Map<String, Object> contentsPart = new HashMap<>();
            contentsPart.put("parts", Collections.singletonList(textPart));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("contents", Collections.singletonList(contentsPart));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List candidates = (List) response.getBody().get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map content = (Map) candidate.get("content");
                    List parts = (List) content.get("parts");
                    Map part = (Map) parts.get(0);
                    return (String) part.get("text");
                }
            }
        } catch (Throwable e) {
            System.err.println("⚠️ [WARN] Detalle al consultar Gemini: " + e.getMessage());
        }

        return generarRespuestaRespaldo(mensajeUsuario);
    }

    private String generarRespuestaRespaldo(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            return "¡Hola! Para ayudarte con la orientación de triaje, por favor cuéntame qué síntomas o molestias sientes hoy.";
        }

        // Elimina tildes/acentos y convierte a minúsculas
        String msg = Normalizer.normalize(mensaje, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase();

        if (msg.contains("cabeza") || msg.contains("fiebre") || msg.contains("gripe") || msg.contains("resfrio") || msg.contains("tos")) {
            return "Entiendo tus síntomas (malestar general / dolor de cabeza). Te sugiero mantenerte bien hidratado, guardar reposo y reservar una cita en **Medicina General** para una evaluación preliminar.";
        } else if (msg.contains("estomago") || msg.contains("abdominal") || msg.contains("nausea") || msg.contains("vomito") || msg.contains("diarrea")) {
            return "Para dolores de estómago, náuseas o molestias digestivas, te recomendamos agendar una consulta con la especialidad de **Gastroenterología** o **Medicina General**.";
        } else if (msg.contains("pecho") || msg.contains("corazon") || msg.contains("presion") || msg.contains("respirar")) {
            return "⚠️ Si sientes dolor en el pecho o dificultad para respirar, te recomendamos acudir de inmediato a **Emergencias** o agendar una cita prioritaria en **Cardiología**.";
        } else if (msg.contains("pie") || msg.contains("pierna") || msg.contains("rodilla") || msg.contains("hueso") || msg.contains("tobillo") || msg.contains("espalda")) {
            return "Para dolores musculares, articulares o molestias en extremidades (como pie, piernas o espalda), te sugerimos agendar una cita en **Traumatología y Ortopedia**.";
        } else if (msg.contains("ojo") || msg.contains("vista") || msg.contains("vision")) {
            return "Para molestias o dolencias oculares, te sugerimos agendar una consulta en **Oftalmología**.";
        } else if (msg.contains("piel") || msg.contains("alergia") || msg.contains("mancha") || msg.contains("picazon")) {
            return "Para afecciones cutáneas, alergias o picazón en la piel, te recomendamos agendar con **Dermatología**.";
        } else if (msg.contains("hola") || msg.contains("buenos") || msg.contains("buenas")) {
            return "¡Hola! 👋 Bienvenido al servicio de triaje del Hospital San José. Por favor cuéntame qué síntomas o molestias sientes para poder orientarte.";
        } else {
            return "Gracias por detallar tus síntomas. Para una evaluación inicial completa, te sugerimos agendar una consulta en **Medicina General**, donde te derivarán al especialista correspondiente.";
        }
    }
}