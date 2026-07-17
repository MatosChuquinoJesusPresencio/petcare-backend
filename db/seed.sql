-- Datos de prueba para PetCare
-- Compatible con H2 en modo PostgreSQL
-- Contrasena de todos los usuarios: "password" (BCrypt: $2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G)
-- Fecha de referencia: 16 de julio de 2026

-- ============================================================
-- TRUNCATE
-- ============================================================
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE notificaciones;
TRUNCATE TABLE seguimientos;
TRUNCATE TABLE consentimientos;
TRUNCATE TABLE plan_tratamiento_actividades;
TRUNCATE TABLE planes_tratamiento;
TRUNCATE TABLE receta_detalle;
TRUNCATE TABLE recetas;
TRUNCATE TABLE historial_vacunaciones;
TRUNCATE TABLE auditoria_clinica;
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
-- 1. USUARIOS (5 - sin DUENOS, no tienen acceso al sistema)
-- ============================================================
INSERT INTO usuarios (id, contrasena, nombres, apellidos, email, telefono, rol, estado) VALUES
(1, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Admin',    'PetCare',   'admin@petcare.com',      '999999001', 'ADMINISTRADOR', true),
(2, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Carlos',   'Mendoza',   'carlos@petcare.com',     '999999002', 'VETERINARIO',   true),
(3, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Ana',      'Gomez',     'ana@petcare.com',        '999999003', 'ASISTENTE',     true),
(7, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Lucia',    'Fernandez', 'lucia@petcare.com',      '999999007', 'VETERINARIO',   true),
(8, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Pedro',    'Ruiz',      'pedro@petcare.com',      '999999008', 'ASISTENTE',     true);

-- ============================================================
-- 2. DUENOS (3 - sin cuenta de usuario, no acceden al sistema)
-- ============================================================
INSERT INTO duenos (id, dni, telefono, direccion, usuario_id) VALUES
(1, '12345678', '999999004', 'Av. Larco 123, Miraflores',     NULL),
(2, '23456789', '999999005', 'Jr. Las Flores 456, San Isidro', NULL),
(3, '34567890', '999999006', 'Calle Los Olivos 789, Surco',    NULL);

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
(1, 'Fido',   'Perro', 'Golden Retriever', 'MACHO',  '2022-05-10', 'MC-000001', 'ESTERILIZADO', 'Penicilina',       'Ninguna',              'Cuidado con la cadera',    NULL,                    true),
(2, 'Luna',   'Gato',  'Siames',           'HEMBRA', '2023-08-15', 'MC-000002', 'INTACTA',      NULL,               NULL,                   NULL,                     'Vacunas al dia',        true),
(3, 'Rex',    'Perro', 'Pastor Aleman',    'MACHO',  '2021-01-20', 'MC-000003', 'ESTERILIZADO', NULL,               'Displasia de cadera',  'Requiere control de peso', 'Dieta especial',        true),
(4, 'Mishi',  'Gato',  'Persa',            'HEMBRA', '2022-11-30', 'MC-000004', 'ESTERILIZADO', 'Pulgas',           'Ninguna',              NULL,                       NULL,                    true),
(5, 'Copito', 'Conejo','Angora',           'MACHO',  '2024-03-15', 'MC-000005', 'INTACTA',      NULL,               NULL,                   NULL,                       NULL,                    true),
(6, 'Toby',   'Perro', 'Labrador',         'MACHO',  '2020-07-22', 'MC-000006', 'ESTERILIZADO', NULL,               'Otitis cronica',       'Revision oidos mensual',   'Usar gotas oticas',    true),
(7, 'Lola',   'Perro', 'Beagle',           'HEMBRA', '2023-12-01', 'MC-000007', 'INTACTA',      'Pollo',            'Ninguna',              'Propensa a obesidad',      'Dieta controlada',     true);

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
-- 7. DISPONIBILIDAD DE VETERINARIOS
-- ============================================================
INSERT INTO disponibilidad_veterinarios (id, veterinario_id, dia_semana, hora_inicio, hora_fin, activo) VALUES
(1, 2, 1, '09:00:00', '18:00:00', true),
(2, 2, 3, '09:00:00', '18:00:00', true),
(3, 2, 5, '09:00:00', '18:00:00', true),
(4, 7, 2, '09:00:00', '18:00:00', true),
(5, 7, 4, '09:00:00', '18:00:00', true),
(6, 7, 6, '09:00:00', '13:00:00', true);

-- ============================================================
-- 8. CITAS (12)
-- ============================================================
INSERT INTO citas (id, mascota_id, veterinario_id, servicio_id, fecha_hora, estado, notas, creado_por, creado_en, actualizado_en) VALUES
(1,  1, 2, 1, TIMESTAMP '2026-07-02 10:00:00', 'ATENDIDA',   'Chequeo anual de Fido',                      3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2,  2, 2, 2, TIMESTAMP '2026-07-04 11:00:00', 'ATENDIDA',   'Vacunacion anual Luna',                      3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3,  3, 7, 5, TIMESTAMP '2026-07-07 09:30:00', 'ATENDIDA',   'Control de peso Rex',                        3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4,  4, 2, 3, TIMESTAMP '2026-07-09 14:00:00', 'ATENDIDA',   'Desparasitacion semestral Mishi',            8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5,  6, 7, 1, TIMESTAMP '2026-07-11 10:00:00', 'ATENDIDA',   'Revision por otitis recurrente Toby',       3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6,  7, 2, 1, TIMESTAMP '2026-07-14 15:00:00', 'ATENDIDA',   'Primera consulta Lola, control general',    8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7,  1, 2, 5, TIMESTAMP '2026-07-16 10:00:00', 'CONFIRMADA', 'Control post-consulta Fido',                 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8,  2, 2, 2, TIMESTAMP '2026-07-17 11:00:00', 'CONFIRMADA', 'Segunda dosis vacuna Luna',                  3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9,  3, 7, 5, TIMESTAMP '2026-07-18 09:30:00', 'AGENDADA',   'Reevaluacion plan tratamiento Rex',          3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 5, 2, 1, TIMESTAMP '2026-07-21 10:00:00', 'AGENDADA',   'Revision general Copito',                    8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 4, 7, 5, TIMESTAMP '2026-07-23 14:00:00', 'AGENDADA',   'Control post-desparasitacion Mishi',         8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 6, 2, 5, TIMESTAMP '2026-07-25 11:00:00', 'AGENDADA',   'Revision oidos Toby, seguimiento otitis',   3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 9. TRIAJES (6)
-- ============================================================
INSERT INTO triajes (id, cita_id, motivo_visita, nivel_urgencia, signos_visibles, observaciones, peso, temperatura, frecuencia_cardiaca, frecuencia_respiratoria, asistente_id, creado_en) VALUES
(1, 1, 'Chequeo anual',                   'RUTINARIA',  'Sin signos visibles',       'Mascota tranquila, cooperativa',          30.5, 38.5, 80, 20, 3, CURRENT_TIMESTAMP),
(2, 2, 'Vacunacion anual',                'RUTINARIA',  'Sin signos visibles',       'Gato activo, peso adecuado',              4.2,  38.2, 90, 22, 8, CURRENT_TIMESTAMP),
(3, 3, 'Control de peso',                 'PREFERENTE', 'Sobrepeso leve',            'Mascota con 32kg, peso ideal 28kg',       32.0, 38.8, 85, 24, 3, CURRENT_TIMESTAMP),
(4, 4, 'Desparasitacion semestral',       'RUTINARIA',  'Sin signos visibles',       'Gato sano, buen estado general',          3.8,  38.0, 88, 20, 8, CURRENT_TIMESTAMP),
(5, 5, 'Otitis recurrente',               'PREFERENTE', 'Inflamacion canal auditivo', 'Inflamacion en oido derecho, prurito',   28.0, 38.4, 78, 18, 3, CURRENT_TIMESTAMP),
(6, 6, 'Primera consulta',                'RUTINARIA',  'Sin signos visibles',       'Cachorra joven, sana',                    8.5,  38.6, 100, 26, 8, CURRENT_TIMESTAMP);

-- ============================================================
-- 10. SALA DE ESPERA (7)
-- ============================================================
INSERT INTO sala_espera (id, cita_id, mascota_id, fecha_llegada, estado, observaciones) VALUES
(1, 1, 1, TIMESTAMP '2026-07-02 09:45:00', 'ATENDIDO',    'Llego temprano, sin novedad'),
(2, 2, 2, TIMESTAMP '2026-07-04 10:50:00', 'ATENDIDO',    NULL),
(3, 3, 3, TIMESTAMP '2026-07-07 09:15:00', 'ATENDIDO',    'Mascota ansiosa'),
(4, 4, 4, TIMESTAMP '2026-07-09 13:45:00', 'ATENDIDO',    NULL),
(5, 5, 6, TIMESTAMP '2026-07-11 09:40:00', 'ATENDIDO',    'Toby inquieto, posiblemente por dolor de oido'),
(6, 6, 7, TIMESTAMP '2026-07-14 14:50:00', 'ATENDIDO',    'Primera vez en la clinica'),
(7, 7, 1, TIMESTAMP '2026-07-16 09:30:00', 'PENDIENTE',   'Control post-consulta, llego puntual');

-- ============================================================
-- 11. ATENCIONES CLINICAS (6)
-- ============================================================
INSERT INTO atenciones_clinicas (id, cita_id, mascota_id, veterinario_id, triaje_id, motivo_consulta, sintomas, diagnostico, tratamiento, observaciones_clinicas, creado_por, creado_en) VALUES
(1, 1, 1, 2, 1, 'Chequeo anual',
    'Ninguno',
    'Paciente sano, sin hallazgos anormales',
    'Mantener alimentacion actual y ejercicio diario. Proxima visita en 6 meses.',
    'Peso y temperatura normales. Vacunas al dia.',
    2, CURRENT_TIMESTAMP),

(2, 2, 2, 2, 2, 'Vacunacion anual',
    'Ninguno',
    'Gata sana, lista para vacunacion',
    'Aplicar vacuna trivalente felina. Proxima dosis en 1 mes.',
    'Sin reacciones adversas previas. Peso ideal.',
    2, CURRENT_TIMESTAMP),

(3, 3, 3, 7, 3, 'Control de peso',
    'Letargia leve, dificultad para subir escaleras',
    'Sobrepeso grado I con inicio de displasia de cadera',
    'Dieta hipocalorica. Ejercicio controlado: caminatas de 20min. Glucosamina suplementaria. Control en 1 mes.',
    'Radiografia de caderas programada. Peso 32kg, ideal 28kg.',
    7, CURRENT_TIMESTAMP),

(4, 4, 4, 2, 4, 'Desparasitacion semestral',
    'Prurito leve en zona lumbar',
    'Parasitosis externa leve (pulgas)',
    'Aplicar antiparasitario de amplio espectro. Tratamiento antipulgas topico.',
    'Se observan pulgas en pelaje. Sin signos de anemia.',
    2, CURRENT_TIMESTAMP),

(5, 5, 6, 7, 5, 'Revision otitis recurrente',
    'Secrecion marrin en oido derecho, mal olor, prurito',
    'Otitis externa bacteriana cronica en oido derecho',
    'Limpieza auricular. Antibiotico otico por 14 dias. Antiinflamatorio oral 7 dias. Control en 2 semanas.',
    'Canal auditivo inflamado, membrana timpanica integra.',
    7, CURRENT_TIMESTAMP),

(6, 6, 7, 2, 6, 'Primera consulta',
    'Ninguno',
    'Paciente sana, peso adecuado para edad',
    'Iniciar calendario de vacunacion. Desparasitacion en 1 semana. Proxima cita en 1 mes.',
    'Cachorra activa, buena condicion corporal. Alerta: alergia al pollo.',
    2, CURRENT_TIMESTAMP);

-- ============================================================
-- 12. BLOQUEOS DE VETERINARIOS (2)
-- ============================================================
INSERT INTO bloqueos_veterinarios (id, veterinario_id, fecha, hora_inicio, hora_fin, motivo) VALUES
(1, 2, '2026-07-18', '14:00:00', '16:00:00', 'Capacitacion interna'),
(2, 7, '2026-07-22', '09:00:00', '12:00:00', 'Reunion medica regional');

-- ============================================================
-- 13. HISTORIAL DE TRANSFERENCIAS (9)
-- ============================================================
INSERT INTO historial_transferencias (id, mascota_id, dueno_nuevo_id, dueno_anterior_id, fecha, motivo, usuario_responsable_id) VALUES
(1,  1, 1, NULL, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(2,  2, 1, NULL, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(3,  3, 1, NULL, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(4,  4, 2, NULL, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(5,  5, 2, NULL, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(6,  6, 3, NULL, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(7,  7, 3, NULL, CURRENT_TIMESTAMP, 'Registro inicial de mascota', 1),
(8,  3, 1, 2, TIMESTAMP '2026-05-10 11:00:00', 'Cambio de titular por fallecimiento del titular anterior', 1),
(9,  7, 3, 1, TIMESTAMP '2026-06-20 14:30:00', 'Reasignacion por solicitud del dueno anterior', 1);

-- ============================================================
-- 14. HISTORIAL DE VACUNACIONES (12)
-- ============================================================
INSERT INTO historial_vacunaciones (id, mascota_id, veterinario_id, tipo, nombre_producto, fecha_aplicacion, dosis, proxima_dosis, lote, fabricante, observaciones, estado, creado_por, creado_en) VALUES
(1,  1, 2, 'VACUNA',          'Rabies Triple',            '2026-01-15', 'Unica',     '2027-01-15', 'RAB-2026-01', 'Zoetis',      'Sin reacciones adversas',              'APLICADA',  2, CURRENT_TIMESTAMP),
(2,  1, 2, 'VACUNA',          'Canina Polivalente',        '2026-02-20', 'Refuerzo',  '2027-02-20', 'POL-2026-02', 'Zoetis',      'Buena tolerancia',                     'APLICADA',  2, CURRENT_TIMESTAMP),
(3,  1, 2, 'DESPARASITACION', 'Antiparasitario General',   '2026-07-02', 'Trimestral','2026-10-02', 'DES-2026-07', 'Merck',       'Durante chequeo anual',                'APLICADA',  2, CURRENT_TIMESTAMP),
(4,  2, 2, 'VACUNA',          'Felina Trivalente',         '2026-02-10', '1ra',       '2026-03-10', 'TRI-2026-02', 'Boehringer', 'Primera dosis, sin reacciones',        'APLICADA',  2, CURRENT_TIMESTAMP),
(5,  2, 2, 'VACUNA',          'Felina Trivalente',         '2026-03-12', '2da',       '2026-04-12', 'TRI-2026-03', 'Boehringer', 'Segunda dosis completada',             'APLICADA',  2, CURRENT_TIMESTAMP),
(6,  2, 2, 'VACUNA',          'FeLV (Leucemia Felina)',    '2026-07-04', 'Unica',     '2027-07-04', 'FEL-2026-07', 'Zoetis',      'Cita vacunacion anual',                'APLICADA',  2, CURRENT_TIMESTAMP),
(7,  3, 7, 'VACUNA',          'Canina Polivalente',        '2025-12-01', 'Refuerzo',  '2026-12-01', 'POL-2025-12', 'Zoetis',      'Buena tolerancia',                     'APLICADA',  7, CURRENT_TIMESTAMP),
(8,  3, 7, 'DESPARASITACION', 'Antiparasitario General',   '2026-07-07', 'Trimestral','2026-10-07', 'DES-2026-07', 'Merck',       'En control de peso',                   'APLICADA',  7, CURRENT_TIMESTAMP),
(9,  6, 7, 'VACUNA',          'Leptospirosis Canina',      '2026-03-20', 'Unica',     '2027-03-20', 'LEP-2026-03', 'Merck',       'Sin reacciones adversas',              'APLICADA',  7, CURRENT_TIMESTAMP),
(10, 6, 7, 'VACUNA',          'Canina Polivalente',        '2026-06-15', 'Anual',     '2027-06-15', 'POL-2026-06', 'Zoetis',      'Refuerzo anual, buena respuesta',      'APLICADA',  7, CURRENT_TIMESTAMP),
(11, 7, 2, 'VACUNA',          'Canina Polivalente',        '2026-07-14', '1ra',       '2026-08-14', 'POL-2026-07', 'Zoetis',      'Primera dosis, cachorra sana',         'APLICADA',  2, CURRENT_TIMESTAMP),
(12, 5, 2, 'VACUNA',          'Myxomatosis Conejo',        '2026-04-10', 'Unica',     '2027-04-10', 'MYX-2026-04', 'Hipra',       'Vacuna especifica para conejos',       'APLICADA',  2, CURRENT_TIMESTAMP);

-- ============================================================
-- 15. RECETAS (3) + DETALLE (6)
-- ============================================================
INSERT INTO recetas (id, atencion_clinica_id, mascota_id, veterinario_id, diagnostico, notas_adicionales, estado, creado_por, creado_en) VALUES
(1, 1, 1, 2, 'Paciente sano',                          'Sin medicacion requerida. Mantener regimen actual.',              'CERRADA',  2, CURRENT_TIMESTAMP),
(2, 4, 4, 2, 'Parasitosis externa leve',               'Administrar con alimento. No banar en 24h post-tratamiento.',    'EMITIDA',  2, CURRENT_TIMESTAMP),
(3, 5, 6, 7, 'Otitis externa bacteriana cronica',      'Completar tratamiento completo. No suspender antibiotico.',      'EMITIDA',  7, CURRENT_TIMESTAMP);

INSERT INTO receta_detalle (id, receta_id, medicamento, dosis, frecuencia, duracion, via_administracion, indicaciones) VALUES
(1, 1, 'Amoxicilina 250mg',  '1 tableta', 'Cada 12 horas', '7 dias',  'Oral', 'Solo si presenta sintomas'),
(2, 1, 'Meloxicam 0.5mg',    '1/2 tableta', 'Cada 24 horas', '5 dias','Oral', 'Solo si presenta dolor'),
(3, 2, 'Fipronil 50mg',      '1 pipeta',  'Mensual',       '3 meses', 'Topico', 'Aplicar en nuca, zona inaccesible'),
(4, 2, 'Ivermectina 1%',     '0.2ml',     'Unica',         '1 dosis', 'Subcutanea', 'Peso 3.8kg, dosis ajustada'),
(5, 3, 'Orbifloxacino otico','4 gotas',   'Cada 12 horas', '14 dias', 'Otico', 'Limpiar oido antes de aplicar'),
(6, 3, 'Meloxicam 0.5mg',    '1 tableta', 'Cada 24 horas', '7 dias',  'Oral', 'Para control del dolor e inflamacion');

-- ============================================================
-- 16. PLANES DE TRATAMIENTO (2) + ACTIVIDADES (5)
-- ============================================================
INSERT INTO planes_tratamiento (id, atencion_clinica_id, mascota_id, veterinario_id, titulo, descripcion, fecha_inicio, fecha_fin_estimada, estado, creado_por, creado_en) VALUES
(1, 3, 3, 7, 'Plan de control de peso y displasia', 'Reducir peso gradualmente y mejorar movilidad articular', DATE '2026-07-07', DATE '2026-10-07', 'ACTIVO', 7, CURRENT_TIMESTAMP),
(2, 5, 6, 7, 'Tratamiento otitis cronica',          'Control de infeccion y prevencion de recaidas',          DATE '2026-07-11', DATE '2026-08-11', 'ACTIVO', 7, CURRENT_TIMESTAMP);

INSERT INTO plan_tratamiento_actividades (id, plan_tratamiento_id, tipo, descripcion, fecha_programada, hora_programada, frecuencia, responsable, estado) VALUES
(1, 1, 'MEDICACION',  'Administrar glucosamina: suplemento oral diario',           DATE '2026-07-07', TIME '08:00:00', 'Diaria',     'Dueno',       'EN_PROGRESO'),
(2, 1, 'EJERCICICIO', 'Caminata controlada: 20 minutos caminata lenta',           DATE '2026-07-07', TIME '07:00:00', 'Diaria',     'Dueno',       'EN_PROGRESO'),
(3, 1, 'CONTROL',     'Reevaluacion veterinaria: control de evolucion en 1 mes',  DATE '2026-08-07', TIME '10:00:00', NULL,         'Veterinario', 'PENDIENTE'),
(4, 2, 'MEDICACION',  'Aplicar gotas oticas cada 12 horas por 14 dias',           DATE '2026-07-11', TIME '08:00:00', 'Cada 12h',   'Dueno',       'EN_PROGRESO'),
(5, 2, 'CONTROL',     'Control otoscopico en 2 semanas',                           DATE '2026-07-25', TIME '10:00:00', NULL,         'Veterinario', 'PENDIENTE');

-- ============================================================
-- 17. CONSENTIMIENTOS (4)
-- ============================================================
INSERT INTO consentimientos (id, mascota_id, dueno_id, atencion_clinica_id, veterinario_id, tipo_procedimiento, descripcion_procedimiento, riesgos_descritos, alternativas, consentido, fecha_consentimiento, dueno_nombre_verificado, testigo_nombre, observaciones, creado_por, creado_en) VALUES
(1, 1, 1, 1, 2, 'Consulta general',
    'Revision medica completa incluyendo examen fisico y laboratorio',
    'Riesgos minimos asociados al examen fisico',
    NULL, true, TIMESTAMP '2026-07-02 09:50:00', 'Juan Perez', 'Ana Gomez', 'Dueno informado y conforme', 2, CURRENT_TIMESTAMP),
(2, 3, 1, 3, 7, 'Radiografia de caderas',
    'Estudio radiografico para evaluar grado de displasia coxofemoral',
    'Exposicion minima a radiacion. Riesgo de sedacion leve si aplica.',
    'Artroscopia diagnostica', true, TIMESTAMP '2026-07-07 09:40:00', 'Juan Perez', NULL, 'Radiografia postergada a proximo control', 7, CURRENT_TIMESTAMP),
(3, 4, 2, 4, 2, 'Desparasitacion',
    'Aplicacion de antiparasitario de amplio espectro',
    'Riesgo muy bajo. Posible somnolencia transitoria.',
    NULL, true, TIMESTAMP '2026-07-09 13:50:00', 'Maria Lopez', 'Pedro Ruiz', NULL, 2, CURRENT_TIMESTAMP),
(4, 6, 3, 5, 7, 'Tratamiento otitis',
    'Limpieza auricular y aplicacion de antibiotico otico',
    'Molestia temporal durante la limpieza. Riesgo de irritacion del canal.',
    NULL, true, TIMESTAMP '2026-07-11 09:50:00', 'Roberto Garcia', 'Ana Gomez', 'Dueno advertido sobre duracion del tratamiento', 7, CURRENT_TIMESTAMP);

-- ============================================================
-- 18. SEGUIMIENTOS (6)
-- ============================================================
INSERT INTO seguimientos (id, atencion_clinica_id, mascota_id, veterinario_id, dueno_notificado_id, tipo, fecha_programada, motivo, resultado, estado, creado_por, creado_en) VALUES
(1, 1, 1, 2, 1, 'TELEFONICO',  TIMESTAMP '2026-07-09 10:00:00', 'Verificar evolucion post-consulta', 'Paciente sin novedad, se mantiene en observacion', 'COMPLETADO', 2, CURRENT_TIMESTAMP),
(2, 4, 4, 2, 2, 'TELEFONICO',  TIMESTAMP '2026-07-16 09:00:00', 'Seguimiento post-desparasitacion', 'Mishi respondio bien, sin efectos adversos',         'COMPLETADO', 2, CURRENT_TIMESTAMP),
(3, 3, 3, 7, 1, 'PRESENCIAL',  TIMESTAMP '2026-08-07 10:00:00', 'Reevaluacion de plan de tratamiento para Rex', NULL, 'PROGRAMADO', 7, CURRENT_TIMESTAMP),
(4, 5, 6, 7, 3, 'PRESENCIAL',  TIMESTAMP '2026-07-25 10:00:00', 'Control otoscopico post-tratamiento',          NULL, 'PROGRAMADO', 7, CURRENT_TIMESTAMP),
(5, 2, 2, 2, 1, 'TELEFONICO',  TIMESTAMP '2026-07-21 11:00:00', 'Verificar tolerancia a segunda dosis vacuna',  NULL, 'PROGRAMADO', 2, CURRENT_TIMESTAMP),
(6, 6, 7, 2, 3, 'TELEFONICO',  TIMESTAMP '2026-07-28 15:00:00', 'Primera vacuna, verificar reacciones',          NULL, 'CANCELADO',  2, CURRENT_TIMESTAMP);

-- ============================================================
-- 19. NOTIFICACIONES (8)
-- ============================================================
INSERT INTO notificaciones (id, tipo, destino_usuario_id, mascota_id, cita_id, canal, mensaje, estado, fecha_envio, leido, creado_en) VALUES
(1, 'CITA_RECORDATORIO',       3, 1, 7, 'PLATAFORMA', 'Recordatorio: Fido tiene una cita de control hoy 16/07 a las 10:00',    'ENVIADO',    TIMESTAMP '2026-07-15 18:00:00', false,  TIMESTAMP '2026-07-15 18:00:00'),
(2, 'CITA_RECORDATORIO',       3, 2, 8, 'PLATAFORMA', 'Recordatorio: Luna tiene cita de vacunacion manana 17/07 a las 11:00', 'ENVIADO',    TIMESTAMP '2026-07-16 08:00:00', false,  TIMESTAMP '2026-07-16 08:00:00'),
(3, 'VACUNA_PROXIMA_DOSIS',    3, 1, NULL, 'PLATAFORMA', 'Proxima dosis de vacuna polivalente de Fido: 20/02/2027',           'ENVIADO',    TIMESTAMP '2026-07-02 10:30:00', true,   TIMESTAMP '2026-07-02 10:30:00'),
(4, 'SEGUIMIENTO_PROGRAMADO',  2, 3, NULL, 'PLATAFORMA', 'Seguimiento programado para Rex el 07/08 - Reevaluacion de displasia','ENVIADO',   TIMESTAMP '2026-07-07 11:00:00', true,   TIMESTAMP '2026-07-07 11:00:00'),
(5, 'TRIAJE_REALIZADO',        2, 1, 7, 'PLATAFORMA', 'Triaje realizado para Fido. Urgencia: RUTINARIA. En sala de espera.',  'ENVIADO',    TIMESTAMP '2026-07-16 09:35:00', false,  TIMESTAMP '2026-07-16 09:35:00'),
(6, 'SEGUIMIENTO_PROGRAMADO',  7, 6, NULL, 'PLATAFORMA', 'Seguimiento programado para Toby el 25/07 - Control otoscopico',     'ENVIADO',    TIMESTAMP '2026-07-11 11:30:00', false,  TIMESTAMP '2026-07-11 11:30:00'),
(7, 'VACUNA_PROXIMA_DOSIS',    3, 7, NULL, 'PLATAFORMA', 'Proxima dosis de polivalente de Lola: 14/08/2026 (2da dosis)',        'ENVIADO',    TIMESTAMP '2026-07-14 15:30:00', true,   TIMESTAMP '2026-07-14 15:30:00'),
(8, 'CITA_RECORDATORIO',       1, 3, 9, 'PLATAFORMA', 'Cita agendada: Rex con Dra. Lucia el 18/07 a las 09:30',              'ENVIADO',    TIMESTAMP '2026-07-16 08:00:00', false,  TIMESTAMP '2026-07-16 08:00:00');

-- ============================================================
-- 20. AUDITORIA CLINICA (6)
-- ============================================================
INSERT INTO auditoria_clinica (id, tabla_afectada, registro_id, campo, valor_anterior, valor_nuevo, tipo_operacion, usuario_id, fecha_cambio, motivo) VALUES
(1, 'mascotas',           1, 'resumen', NULL,       'Creacion mascota Fido (Perro, Golden Retriever)', 'INSERT', 1, TIMESTAMP '2026-01-10 09:00:00', 'Registro inicial'),
(2, 'mascotas',           3, 'resumen', NULL,       'Creacion mascota Rex (Perro, Pastor Aleman)',     'INSERT', 1, TIMESTAMP '2026-01-10 09:15:00', 'Registro inicial'),
(3, 'citas',              7, 'resumen', NULL,       'Creacion cita #7 - Fido control 16/07 10:00',    'INSERT', 3, TIMESTAMP '2026-07-10 14:00:00', 'Agendamiento de control'),
(4, 'citas',              7, 'estado',  'AGENDADA', 'CONFIRMADA',                                     'UPDATE', 3, TIMESTAMP '2026-07-12 10:00:00', 'Confirmacion telefonica con dueno'),
(5, 'atenciones_clinicas', 3, 'resumen', NULL,      'Atencion Rex: sobrepeso + displasia grado I',    'INSERT', 7, TIMESTAMP '2026-07-07 10:00:00', 'Registro de atencion clinica'),
(6, 'mascotas',           3, 'notas_medicas', NULL, 'Dieta especial y control de peso',               'UPDATE', 7, TIMESTAMP '2026-07-07 10:15:00', 'Actualizacion de notas post-diagnostico'),
(7, 'usuarios',           2, 'resumen', NULL,        'Creacion usuario Carlos Mendoza (VETERINARIO)',  'INSERT', 1, TIMESTAMP '2026-01-05 08:00:00', 'Alta de personal veterinario'),
(8, 'usuarios',           3, 'resumen', NULL,        'Creacion usuario Ana Gomez (ASISTENTE)',         'INSERT', 1, TIMESTAMP '2026-01-05 08:15:00', 'Alta de personal asistente'),
(9, 'usuarios',           7, 'resumen', NULL,        'Creacion usuario Lucia Fernandez (VETERINARIO)', 'INSERT', 1, TIMESTAMP '2026-03-10 09:00:00', 'Alta de personal veterinario'),
(10, 'usuarios',          2, 'rol',      'VETERINARIO', 'VETERINARIO',                                 'UPDATE', 1, TIMESTAMP '2026-07-15 11:00:00', 'Verificacion de rol');

-- ============================================================
-- Reset de secuencias
-- ============================================================
ALTER TABLE usuarios ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM usuarios);
ALTER TABLE duenos ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM duenos);
ALTER TABLE mascotas ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM mascotas);
ALTER TABLE servicios ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM servicios);
ALTER TABLE citas ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM citas);
ALTER TABLE triajes ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM triajes);
ALTER TABLE atenciones_clinicas ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM atenciones_clinicas);
ALTER TABLE historial_vacunaciones ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM historial_vacunaciones);
ALTER TABLE recetas ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM recetas);
ALTER TABLE receta_detalle ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM receta_detalle);
ALTER TABLE planes_tratamiento ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM planes_tratamiento);
ALTER TABLE plan_tratamiento_actividades ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM plan_tratamiento_actividades);
ALTER TABLE consentimientos ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM consentimientos);
ALTER TABLE seguimientos ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM seguimientos);
ALTER TABLE notificaciones ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM notificaciones);
ALTER TABLE auditoria_clinica ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM auditoria_clinica);
ALTER TABLE sala_espera ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM sala_espera);
ALTER TABLE bloqueos_veterinarios ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM bloqueos_veterinarios);
ALTER TABLE disponibilidad_veterinarios ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM disponibilidad_veterinarios);
ALTER TABLE historial_transferencias ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM historial_transferencias);
ALTER TABLE contactos_emergencia ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM contactos_emergencia);
ALTER TABLE mascota_responsables ALTER COLUMN id RESTART WITH (SELECT COALESCE(MAX(id), 0) + 1 FROM mascota_responsables);
