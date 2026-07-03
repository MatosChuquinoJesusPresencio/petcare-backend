-- Datos iniciales de prueba para PetCare
-- Compatible con H2 en modo PostgreSQL

-- ============================================================
-- 1. USUARIOS (contraseña: "password" en todos)
-- BCrypt hash: $2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G
-- ============================================================
INSERT INTO usuarios (id, contrasena, nombres, apellidos, email, telefono, rol, estado) VALUES
(1, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Admin',    'PetCare',   'admin@petcare.com',      '999999999', 'ADMINISTRADOR', true),
(2, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Carlos',   'Mendoza',   'carlos@petcare.com',     '988888888', 'VETERINARIO',   true),
(3, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Ana',      'Gomez',     'ana@petcare.com',        '977777777', 'ASISTENTE',     true),
(4, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Juan',     'Perez',     'juan.perez@gmail.com',   '966666666', 'DUENO',         true);

-- ============================================================
-- 2. DUEÑOS
-- ============================================================
INSERT INTO duenos (id, dni, telefono, direccion, usuario_id) VALUES
(1, '12345678', '966666666', 'Av. Larco 123, Miraflores', 4);

-- ============================================================
-- 3. CONTACTOS DE EMERGENCIA
-- ============================================================
INSERT INTO contactos_emergencia (id, dueno_id, nombre, telefono, relacion) VALUES
(1, 1, 'Maria Perez', '955555555', 'Hermana');

-- ============================================================
-- 4. MASCOTAS
-- ============================================================
INSERT INTO mascotas (id, nombre, especie, raza, genero, fecha_nacimiento, microchip, condicion_reproductiva, alergias, enfermedades_cronicas, alertas_medicas, estado) VALUES
(1, 'Fido',   'Perro', 'Golden Retriever', 'MACHO', '2022-05-10', 'MC-12345', 'ESTERILIZADO', 'Penicilina', 'Ninguna', 'Cuidado con la cadera', true),
(2, 'Luna',   'Gato',  'Siamés',           'HEMBRA', '2023-08-15', 'MC-12346', 'INTACTA',       NULL,          NULL,      NULL,                    true),
(3, 'Rex',    'Perro', 'Pastor Alemán',    'MACHO', '2021-01-20', 'MC-12347', 'ESTERILIZADO', NULL,          'Displasia de cadera', 'Requiere control de peso', true);

-- ============================================================
-- 5. RELACIÓN MASCOTA-DUEÑO
-- ============================================================
INSERT INTO mascota_responsables (id, mascota_id, dueno_id, es_principal, relacion) VALUES
(1, 1, 1, true, 'Propietario'),
(2, 2, 1, true, 'Propietario'),
(3, 3, 1, true, 'Propietario');

-- ============================================================
-- 6. SERVICIOS
-- ============================================================
INSERT INTO servicios (id, nombre, descripcion, duracion_minutos, costo_referencial, activo) VALUES
(1, 'Consulta General',  'Revisión médica general para la mascota',          30,  80.00,  true),
(2, 'Vacunación',        'Aplicación de vacunas según calendario',           20,  50.00,  true),
(3, 'Desparasitación',   'Desparasitación interna y externa',                15,  30.00,  true),
(4, 'Cirugía Menor',     'Procedimientos quirúrgicos ambulatorios sencillos', 60, 300.00, true),
(5, 'Control',           'Cita de seguimiento para evaluar evolución',       20,  40.00,  true),
(6, 'Emergencia',        'Atención médica inmediata de urgencia',            45,  150.00, true);

-- ============================================================
-- 7. DISPONIBILIDAD DE VETERINARIOS (veterinario_id = 2 Carlos)
-- Lunes(1), Miércoles(3), Viernes(5) de 9:00 AM a 6:00 PM
-- ============================================================
INSERT INTO disponibilidad_veterinarios (id, veterinario_id, dia_semana, hora_inicio, hora_fin, activo) VALUES
(1, 2, 1, '09:00:00', '18:00:00', true),
(2, 2, 3, '09:00:00', '18:00:00', true),
(3, 2, 5, '09:00:00', '18:00:00', true);

-- ============================================================
-- 8. CITAS
-- ============================================================
INSERT INTO citas (id, mascota_id, veterinario_id, servicio_id, fecha_hora, estado, notas, creado_por, creado_en, actualizado_en) VALUES
(1, 1, 2, 1, TIMESTAMP '2026-07-03 10:00:00', 'PENDIENTE', 'Primera consulta general para Fido',  3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, 2, TIMESTAMP '2026-07-03 11:00:00', 'CONFIRMADA', 'Vacunación anual para Luna',          3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, 2, 5, TIMESTAMP '2026-07-04 09:30:00', 'PENDIENTE', 'Control de peso para Rex',            3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 9. TRIajes
-- ============================================================
INSERT INTO triajes (id, cita_id, motivo_visita, nivel_urgencia, signos_visibles, observaciones, peso, temperatura, frecuencia_cardiaca, frecuencia_respiratoria, asistente_id, creado_en) VALUES
(1, 1, 'Revisión general y chequeo anual', 'RUTINARIA', 'Sin signos visibles', 'Mascota tranquila y cooperativa', 30.5, 38.5, 80, 20, 3, CURRENT_TIMESTAMP),
(2, 2, 'Vacunación anual', 'RUTINARIA', 'Sin signos visibles', NULL, 4.2, 38.2, 90, 22, 3, CURRENT_TIMESTAMP);

-- ============================================================
-- 10. SALA DE ESPERA
-- ============================================================
INSERT INTO sala_espera (id, cita_id, mascota_id, fecha_llegada, estado, observaciones) VALUES
(1, 1, 1, TIMESTAMP '2026-07-03 09:45:00', 'EN_ATENCION', 'Llegó temprano'),
(2, 2, 2, TIMESTAMP '2026-07-03 10:50:00', 'PENDIENTE',   NULL);

-- ============================================================
-- 11. ATENCIONES CLÍNICAS
-- ============================================================
INSERT INTO atenciones_clinicas (id, cita_id, mascota_id, veterinario_id, triaje_id, motivo_consulta, sintomas, diagnostico, tratamiento, observaciones_clinicas, creado_por, creado_en) VALUES
(1, 1, 1, 2, 1, 'Chequeo anual', 'Ninguno', 'Paciente sano, sin hallazgos anormales', 'Mantener alimentación actual y ejercicio diario. Próxima visita en 6 meses.', 'Peso y temperatura normales. Vacunas al día.', 2, CURRENT_TIMESTAMP);

-- ============================================================
-- 12. BLOQUEOS DE VETERINARIOS
-- ============================================================
INSERT INTO bloqueos_veterinarios (id, veterinario_id, fecha, hora_inicio, hora_fin, motivo) VALUES
(1, 2, '2026-07-10', '14:00:00', '16:00:00', 'Capacitación interna');

-- ============================================================
-- 13. HISTORIAL DE TRANSFERENCIAS
-- ============================================================
INSERT INTO historial_transferencias (id, mascota_id, dueno_nuevo_id, fecha, motivo, usuario_responsable_id) VALUES
(1, 1, 1, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(2, 2, 1, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(3, 3, 1, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1);
