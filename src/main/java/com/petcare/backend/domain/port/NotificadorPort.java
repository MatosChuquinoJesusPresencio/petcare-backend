package com.petcare.backend.domain.port;

public interface NotificadorPort {
    boolean enviarWhatsApp(String telefono, String mensaje);
    boolean enviarSMS(String telefono, String mensaje);
}
