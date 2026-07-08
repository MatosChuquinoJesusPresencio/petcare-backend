-- Datos iniciales de prueba para PetCare
-- Compatible con H2 en modo PostgreSQL
-- Todas las contrasenas: "password" (BCrypt: $2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G)

-- ============================================================
-- TRUNCATE: limpia todos los datos existentes
-- ============================================================
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE historial_transferencias;
TRUNCATE TABLE bloqueos_veterinarios;
TRUNCATE TABLE atenciones_clinicas;
TRUNCATE TABLE sala_espera;
TRUNCATE TABLE triajes;
TRUNCATE TABLE citas;
TRUNCATE TABLE disponibilidad_veterinarios;
TRUNCATE TABLE mascota_responsables;
TRUNCATE TABLE servicios;
TRUNCATE TABLE contactos_emergencia;
TRUNCATE TABLE mascotas;
TRUNCATE TABLE duenos;
TRUNCATE TABLE usuarios;

SET REFERENTIAL_INTEGRITY TRUE;

-- ============================================================
-- 1. USUARIOS (8)
-- Admin(1), Carlos(2 vet), Ana(3 asis), Juan(4 dueno), Maria(5 dueno),
-- Roberto(6 dueno), Lucia(7 vet), Pedro(8 asis)
-- ============================================================
INSERT INTO usuarios (id, contrasena, nombres, apellidos, email, telefono, rol, estado, token_version) VALUES
(1, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Admin',    'PetCare',   'admin@petcare.com',      '999999001', 'ADMINISTRADOR', true, 0),
(2, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Carlos',   'Mendoza',   'carlos@petcare.com',     '999999002', 'VETERINARIO',   true, 0),
(3, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Ana',      'Gomez',     'ana@petcare.com',        '999999003', 'ASISTENTE',     true, 0),
(4, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Juan',     'Perez',     'juan.perez@gmail.com',   '999999004', 'DUENO',         true, 0),
(5, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Maria',    'Lopez',     'maria.lopez@gmail.com',  '999999005', 'DUENO',         true, 0),
(6, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Roberto',  'Garcia',    'roberto.garcia@gmail.com','999999006','DUENO',         true, 0),
(7, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Lucia',    'Fernandez', 'lucia@petcare.com',      '999999007', 'VETERINARIO',   true, 0),
(8, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Pedro',    'Ruiz',      'pedro@petcare.com',      '999999008', 'ASISTENTE',     true, 0);

-- ============================================================
-- 2. DUENOS (3)
-- ============================================================
INSERT INTO duenos (id, dni, telefono, direccion, usuario_id) VALUES
(1, '12345678', '999999004', 'Av. Larco 123, Miraflores',    4),
(2, '23456789', '999999005', 'Jr. Las Flores 456, San Isidro', 5),
(3, '34567890', '999999006', 'Calle Los Olivos 789, Surco',    6);

-- ============================================================
-- 3. CONTACTOS DE EMERGENCIA (3)
-- ============================================================
INSERT INTO contactos_emergencia (id, dueno_id, nombre, telefono, relacion) VALUES
(1, 1, 'Maria Perez',   '955555001', 'Hermana'),
(2, 2, 'Carlos Lopez',  '955555002', 'Hermano'),
(3, 3, 'Ana Garcia',    '955555003', 'Esposa');

-- ============================================================
-- 4. MASCOTAS (7)
-- ============================================================
INSERT INTO mascotas (id, nombre, especie, raza, genero, fecha_nacimiento, microchip, condicion_reproductiva, alergias, enfermedades_cronicas, alertas_medicas, notas_medicas, estado) VALUES
(1, 'Fido',  'Perro', 'Golden Retriever', 'MACHO', '2022-05-10', 'MC-000001', 'ESTERILIZADO', 'Penicilina', 'Ninguna',          'Cuidado con la cadera',    NULL,                  true),
(2, 'Luna',  'Gato',  'Siamés',           'HEMBRA', '2023-08-15', 'MC-000002', 'INTACTA',       NULL,          NULL,              NULL,                       'Vacunas al dia',      true),
(3, 'Rex',   'Perro', 'Pastor Alemán',    'MACHO', '2021-01-20', 'MC-000003', 'ESTERILIZADO',  NULL,         'Displasia de cadera', 'Requiere control de peso', 'Dieta especial',      true),
(4, 'Mishi', 'Gato',  'Persa',            'HEMBRA', '2022-11-30', 'MC-000004', 'ESTERILIZADO',  'Pulgas',     'Ninguna',          NULL,                       NULL,                  true),
(5, 'Copito','Conejo','Angora',           'MACHO', '2024-03-15', 'MC-000005', 'INTACTA',       NULL,          NULL,              NULL,                       NULL,                  true),
(6, 'Toby',  'Perro', 'Labrador',         'MACHO', '2020-07-22', 'MC-000006', 'ESTERILIZADO',  NULL,         'Otitis cronica',   'Revision oidos mensual',   'Usar gotas oticas',   true),
(7, 'Lola',  'Perro', 'Beagle',           'HEMBRA', '2023-12-01','MC-000007', 'INTACTA',       'Pollo',      'Ninguna',          'Propensa a obesidad',      'Dieta controlada',    true);

-- ============================================================
-- 5. RELACION MASCOTA-DUENO (7)
-- ============================================================
INSERT INTO mascota_responsables (id, mascota_id, dueno_id, es_principal, relacion) VALUES
(1, 1, 1, true, 'Propietario'),
(2, 2, 1, true, 'Propietario'),
(3, 3, 1, true, 'Propietario'),
(4, 4, 2, true, 'Propietario'),
(5, 5, 2, true, 'Propietario'),
(6, 6, 3, true, 'Propietario'),
(7, 7, 3, true, 'Propietario');

-- ============================================================
-- 6. SERVICIOS (6)
-- ============================================================
INSERT INTO servicios (id, nombre, descripcion, duracion_minutos, costo_referencial, activo) VALUES
(1, 'Consulta General', 'Revision medica general para la mascota',           30,  80.00,  true),
(2, 'Vacunacion',       'Aplicacion de vacunas segun calendario',            20,  50.00,  true),
(3, 'Desparasitacion',  'Desparasitacion interna y externa',                 15,  30.00,  true),
(4, 'Cirugia Menor',    'Procedimientos quirurgicos ambulatorios sencillos', 60,  300.00, true),
(5, 'Control',          'Cita de seguimiento para evaluar evolucion',        20,  40.00,  true),
(6, 'Emergencia',       'Atencion medica inmediata de urgencia',             45,  150.00, true);

-- ============================================================
-- 7. DISPONIBILIDAD DE VETERINARIOS (6)
-- ============================================================
INSERT INTO disponibilidad_veterinarios (id, veterinario_id, dia_semana, hora_inicio, hora_fin, activo) VALUES
(1, 2, 1, '09:00:00', '18:00:00', true),
(2, 2, 3, '09:00:00', '18:00:00', true),
(3, 2, 5, '09:00:00', '18:00:00', true),
(4, 7, 2, '09:00:00', '18:00:00', true),
(5, 7, 4, '09:00:00', '18:00:00', true),
(6, 7, 6, '09:00:00', '13:00:00', true);

-- ============================================================
-- 8. CITAS (9)
-- ============================================================
INSERT INTO citas (id, mascota_id, veterinario_id, servicio_id, fecha_hora, estado, notas, creado_por, creado_en, actualizado_en) VALUES
(1, 1, 2, 1, TIMESTAMP '2026-07-03 10:00:00', 'AGENDADA',  'Primera consulta general para Fido',  3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, 2, 2, TIMESTAMP '2026-07-03 11:00:00', 'CONFIRMADA','Vacunacion anual para Luna',          3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 4, 2, 3, TIMESTAMP '2026-07-03 15:00:00', 'CONFIRMADA','Desparasitacion para Mishi',          8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 6, 7, 1, TIMESTAMP '2026-07-04 09:30:00', 'AGENDADA',  'Consulta por otitis recurrente',      3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 3, 2, 5, TIMESTAMP '2026-07-06 09:30:00', 'AGENDADA',  'Control de peso para Rex',            3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 5, 2, 1, TIMESTAMP '2026-07-06 11:00:00', 'AGENDADA',  'Revision general para Copito',        8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 1, 7, 5, TIMESTAMP '2026-07-07 10:00:00', 'AGENDADA',  'Control de seguimiento para Fido',    3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 2, 2, 5, TIMESTAMP '2026-07-08 14:00:00', 'CONFIRMADA','Control de vacunas para Luna',         8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 7, 7, 1, TIMESTAMP '2026-07-09 10:00:00', 'AGENDADA',  'Primera consulta para Lola',          3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 9. TRIAJES (2)
-- ============================================================
INSERT INTO triajes (id, cita_id, motivo_visita, nivel_urgencia, signos_visibles, observaciones, peso, temperatura, frecuencia_cardiaca, frecuencia_respiratoria, asistente_id, creado_en) VALUES
(1, 1, 'Revision general y chequeo anual', 'RUTINARIA', 'Sin signos visibles', 'Mascota tranquila y cooperativa', 30.5, 38.5, 80, 20, 3, CURRENT_TIMESTAMP),
(2, 2, 'Vacunacion anual',                'RUTINARIA', 'Sin signos visibles', NULL,                             4.2,  38.2, 90, 22, 3, CURRENT_TIMESTAMP);

-- ============================================================
-- 10. SALA DE ESPERA (2)
-- ============================================================
INSERT INTO sala_espera (id, cita_id, mascota_id, fecha_llegada, estado, observaciones) VALUES
(1, 1, 1, TIMESTAMP '2026-07-03 09:45:00', 'EN_ATENCION', 'Llego temprano'),
(2, 2, 2, TIMESTAMP '2026-07-03 10:50:00', 'PENDIENTE',   NULL);

-- ============================================================
-- 11. ATENCIONES CLINICAS (1)
-- ============================================================
INSERT INTO atenciones_clinicas (id, cita_id, mascota_id, veterinario_id, triaje_id, motivo_consulta, sintomas, diagnostico, tratamiento, observaciones_clinicas, creado_por, creado_en) VALUES
(1, 1, 1, 2, 1, 'Chequeo anual', 'Ninguno', 'Paciente sano, sin hallazgos anormales', 'Mantener alimentacion actual y ejercicio diario. Proxima visita en 6 meses.', 'Peso y temperatura normales. Vacunas al dia.', 2, CURRENT_TIMESTAMP);

-- ============================================================
-- 12. BLOQUEOS DE VETERINARIOS (1)
-- ============================================================
INSERT INTO bloqueos_veterinarios (id, veterinario_id, fecha, hora_inicio, hora_fin, motivo) VALUES
(1, 2, '2026-07-10', '14:00:00', '16:00:00', 'Capacitacion interna');

-- ============================================================
-- 13. HISTORIAL DE TRANSFERENCIAS (7)
-- ============================================================
INSERT INTO historial_transferencias (id, mascota_id, dueno_nuevo_id, fecha, motivo, usuario_responsable_id) VALUES
(1, 1, 1, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(2, 2, 1, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(3, 3, 1, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(4, 4, 2, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(5, 5, 2, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(6, 6, 3, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(7, 7, 3, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1);
