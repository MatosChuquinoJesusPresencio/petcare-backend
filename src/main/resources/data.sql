-- Datos iniciales de prueba para PetCare

-- 1. Usuarios (Password es 'password' en todos)
-- Hashed: $2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G
INSERT INTO usuarios (username, password, nombre, apellido, email, telefono, rol, activo)
VALUES ('admin', '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Admin', 'PetCare',
        'admin@petcare.com', '999999999', 'ADMINISTRADOR', true),
       ('veterinario1', '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Carlos', 'Mendoza',
        'carlos@petcare.com', '988888888', 'VETERINARIO', true),
       ('asistente1', '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Ana', 'Gomez', 'ana@petcare.com',
        '977777777', 'ASISTENTE', true),
       ('dueno1', '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Juan', 'Perez',
        'juan.perez@gmail.com', '966666666', 'DUENO', true),
       ('dueno2', '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Lucia', 'Torres',
        'lucia.torres@gmail.com', '955111222', 'DUENO', true);

-- 2. Dueños
INSERT INTO duenos (nombre, apellido, dni, email, telefono, direccion, usuario_id, activo)
VALUES ('Juan', 'Perez', '12345678', 'juan.perez@gmail.com', '966666666', 'Av. Larco 123, Miraflores', 4, true),
       ('Lucia', 'Torres', '87654321', 'lucia.torres@gmail.com', '955111222', 'Jr. Las Flores 456, Surco', 5, true);

-- 3. Contactos de Emergencia
INSERT INTO contactos_emergencia (dueno_id, nombre, telefono, relacion)
VALUES (1, 'Maria Perez', '955555555', 'Hermana');

-- 4. Mascotas
INSERT INTO mascotas (nombre, especie, raza, sexo, fecha_nacimiento, microchip, condicion_reproductiva, alergias,
                      enfermedades_cronicas, alertas_medicas, activo)
VALUES ('Fido', 'Perro', 'Golden Retriever', 'MACHO', '2022-05-10', 'MC-12345', 'ESTERILIZADO', 'Penicilina', 'Ninguna',
        'Cuidado con la cadera', true),
       ('Mishi', 'Gato', 'Mestizo', 'HEMBRA', '2021-11-03', 'MC-54321', 'ESTERILIZADO', 'Ninguna', 'Asma felina leve',
        'Evitar estrés prolongado', true);

-- 5. Relación Mascota-Dueño
INSERT INTO mascota_responsables (mascota_id, dueno_id, es_principal, relacion)
VALUES (1, 1, true, 'Propietario'),
       (2, 2, true, 'Propietario');

-- 6. Servicios
INSERT INTO servicios (nombre, descripcion, duracion_minutos, costo_referencial, activo)
VALUES ('Baño Medicado', 'Baño para la mascota', 30, 50.00, true),
       ('Consulta General', 'Revisión médica general para la mascota', 30, 80.00, true),
       ('Vacunación', 'Aplicación de vacunas según calendario', 20, 50.00, true),
       ('Desparasitación', 'Desparasitación interna y externa', 15, 30.00, true),
       ('Cirugía Menor', 'Procedimientos quirúrgicos ambulatorios sencillos', 60, 300.00, true),
       ('Control', 'Cita de seguimiento para evaluar evolución', 20, 40.00, true),
       ('Emergencia', 'Atención médica inmediata de urgencia', 45, 150.00, true);

-- 7. Disponibilidad de Veterinarios (veterinario1 = id 2)
-- Lunes (1), Miércoles (3), Viernes (5) de 9:00 AM a 6:00 PM
INSERT INTO disponibilidad_veterinarios (veterinario_id, dia_semana, hora_inicio, hora_fin, activo)
VALUES (2, 1, '09:00:00', '18:00:00', true),
       (2, 3, '09:00:00', '18:00:00', true),
       (2, 5, '09:00:00', '18:00:00', true);

-- 8. Bloqueos de Veterinario
-- Bloqueo para probar GET /api/citas/disponibilidad y validaciones de agenda/reprogramación
INSERT INTO bloqueos_veterinarios (veterinario_id, fecha, hora_inicio, hora_fin, motivo)
VALUES (2, '2026-07-01', '12:00:00', '13:00:00', 'Reunión clínica interna');

-- 9. Citas
INSERT INTO citas (mascota_id, veterinario_id, servicio_id, fecha_hora, estado, notas, creado_por, creado_en, actualizado_en)
VALUES (1, 2, 2, '2026-07-01T09:00:00', 'AGENDADA', 'Chequeo general y revisión de alergias', 3, '2026-06-28T10:00:00', '2026-06-28T10:00:00'),
       (2, 2, 3, '2026-07-01T10:00:00', 'CONFIRMADA', 'Vacuna anual pendiente', 1, '2026-06-28T10:30:00', '2026-06-29T08:15:00'),
       (1, 2, 6, '2026-07-01T15:00:00', 'ATENDIDA', 'Control post tratamiento', 3, '2026-06-28T11:00:00', '2026-07-01T15:45:00'),
       (2, 2, 2, '2026-07-03T09:30:00', 'REPROGRAMADA', 'Consulta movida por solicitud del cliente', 3, '2026-06-29T09:00:00', '2026-06-30T16:20:00'),
       (1, 2, 4, '2026-07-03T11:00:00', 'CANCELADA', 'Desparasitación cancelada por el dueño', 1, '2026-06-29T09:30:00', '2026-06-30T09:00:00'),
       (2, 2, 1, '2026-07-03T16:00:00', 'NO_ASISTIO', 'No se presentó a la cita programada', 3, '2026-06-29T10:00:00', '2026-07-03T16:45:00');
