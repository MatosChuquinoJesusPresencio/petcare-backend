package com.petcare.backend.persistence.adapter;

import com.petcare.backend.domain.port.NotificadorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class TextBeeAdapter implements NotificadorPort {

    private static final Logger log = LoggerFactory.getLogger(TextBeeAdapter.class);

    private final RestTemplate restTemplate;

    @Value("${textbee.api.key:}")
    private String apiKey;

    @Value("${textbee.gateway.id:}")
    private String gatewayId;

    @Value("${textbee.api.url:https://api.textbee.dev/api/v1}")
    private String apiUrl;

    public TextBeeAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean enviarWhatsApp(String telefono, String mensaje) {
        return enviar(telefono, mensaje, "WHATSAPP");
    }

    @Override
    public boolean enviarSMS(String telefono, String mensaje) {
        return enviar(telefono, mensaje, "SMS");
    }

    private boolean enviar(String telefono, String mensaje, String canal) {
        if (apiKey == null || apiKey.isBlank()) {
            log.warn("[TextBee] API key no configurada. Notificacion {} no enviada a {}", canal, telefono);
            return false;
        }
        try {
            String url = apiUrl + "/gateway/devices/" + gatewayId + "/send-sms";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);

            Map<String, Object> body = Map.of(
                    "recipients", new String[]{telefono},
                    "message", mensaje
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("[TextBee] Notificacion {} enviada a {}. Status: {}", canal, telefono, response.getStatusCode());
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("[TextBee] Error enviando {} a {}: {}", canal, telefono, e.getMessage());
            return false;
        }
    }
}
