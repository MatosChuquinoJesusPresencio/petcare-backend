-- Esquema completo + datos de prueba para PetCare
-- Ejecutar en SQL Editor de Supabase (una sola vez)
-- Todas las contrasenas: "password" (BCrypt: $2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G)

-- ============================================================
-- USUARIOS
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id              BIGSERIAL PRIMARY KEY,
    contrasena      VARCHAR(100) NOT NULL,
    nombres         VARCHAR(50) NOT NULL,
    apellidos       VARCHAR(50) NOT NULL,
    email           VARCHAR(100) NOT NULL UNIQUE,
    telefono        VARCHAR(20),
    rol             VARCHAR(30) NOT NULL,
    estado          BOOLEAN NOT NULL DEFAULT TRUE,
    token_version   INTEGER NOT NULL DEFAULT 0
);

-- ============================================================
-- DUENOS
-- ============================================================
CREATE TABLE IF NOT EXISTS duenos (
    id          BIGSERIAL PRIMARY KEY,
    dni         VARCHAR(20) NOT NULL UNIQUE,
    telefono    VARCHAR(20),
    direccion   VARCHAR(200),
    usuario_id  BIGINT REFERENCES usuarios(id)
);

-- ============================================================
-- CONTACTOS DE EMERGENCIA
-- ============================================================
CREATE TABLE IF NOT EXISTS contactos_emergencia (
    id          BIGSERIAL PRIMARY KEY,
    dueno_id    BIGINT NOT NULL REFERENCES duenos(id),
    nombre      VARCHAR(100) NOT NULL,
    telefono    VARCHAR(20) NOT NULL,
    relacion    VARCHAR(50)
);

-- ============================================================
-- MASCOTAS
-- ============================================================
CREATE TABLE IF NOT EXISTS mascotas (
    id                      BIGSERIAL PRIMARY KEY,
    nombre                  VARCHAR(50) NOT NULL,
    especie                 VARCHAR(50) NOT NULL,
    raza                    VARCHAR(50) NOT NULL,
    genero                  VARCHAR(10) NOT NULL,
    fecha_nacimiento        DATE NOT NULL,
    microchip               VARCHAR(50) UNIQUE,
    condicion_reproductiva  VARCHAR(50),
    alergias                TEXT,
    enfermedades_cronicas   TEXT,
    alertas_medicas         TEXT,
    notas_medicas           TEXT,
    estado                  BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- RELACION MASCOTA-DUENO
-- ============================================================
CREATE TABLE IF NOT EXISTS mascota_responsables (
    id            BIGSERIAL PRIMARY KEY,
    mascota_id    BIGINT NOT NULL REFERENCES mascotas(id),
    dueno_id      BIGINT NOT NULL REFERENCES duenos(id),
    es_principal  BOOLEAN NOT NULL DEFAULT FALSE,
    relacion      VARCHAR(50)
);

-- ============================================================
-- SERVICIOS
-- ============================================================
CREATE TABLE IF NOT EXISTS servicios (
    id                BIGSERIAL PRIMARY KEY,
    nombre            VARCHAR(100) NOT NULL,
    descripcion       TEXT,
    duracion_minutos  INTEGER NOT NULL,
    costo_referencial DECIMAL(10,2) NOT NULL,
    activo            BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- DISPONIBILIDAD DE VETERINARIOS
-- ============================================================
CREATE TABLE IF NOT EXISTS disponibilidad_veterinarios (
    id              BIGSERIAL PRIMARY KEY,
    veterinario_id  BIGINT NOT NULL REFERENCES usuarios(id),
    dia_semana      INTEGER NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,
    activo          BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- CITAS
-- ============================================================
CREATE TABLE IF NOT EXISTS citas (
    id                BIGSERIAL PRIMARY KEY,
    mascota_id        BIGINT NOT NULL REFERENCES mascotas(id),
    veterinario_id    BIGINT NOT NULL REFERENCES usuarios(id),
    servicio_id       BIGINT NOT NULL REFERENCES servicios(id),
    fecha_hora        TIMESTAMPTZ NOT NULL,
    estado            VARCHAR(30) NOT NULL,
    notas             TEXT,
    creado_por        BIGINT NOT NULL REFERENCES usuarios(id),
    creado_en         TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    actualizado_por   BIGINT REFERENCES usuarios(id),
    actualizado_en    TIMESTAMPTZ
);

-- ============================================================
-- TRIAJES
-- ============================================================
CREATE TABLE IF NOT EXISTS triajes (
    id                      BIGSERIAL PRIMARY KEY,
    cita_id                 BIGINT NOT NULL UNIQUE REFERENCES citas(id),
    motivo_visita           TEXT NOT NULL,
    nivel_urgencia          VARCHAR(30) NOT NULL,
    signos_visibles         TEXT,
    observaciones           TEXT,
    peso                    DECIMAL(5,2),
    temperatura             DECIMAL(4,1),
    frecuencia_cardiaca     INTEGER,
    frecuencia_respiratoria INTEGER,
    asistente_id            BIGINT NOT NULL REFERENCES usuarios(id),
    creado_en               TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    actualizado_en          TIMESTAMPTZ
);

-- ============================================================
-- SALA DE ESPERA
-- ============================================================
CREATE TABLE IF NOT EXISTS sala_espera (
    id              BIGSERIAL PRIMARY KEY,
    cita_id         BIGINT NOT NULL UNIQUE REFERENCES citas(id),
    mascota_id      BIGINT NOT NULL REFERENCES mascotas(id),
    fecha_llegada   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    estado          VARCHAR(30) NOT NULL,
    observaciones   TEXT
);

-- ============================================================
-- ATENCIONES CLINICAS
-- ============================================================
CREATE TABLE IF NOT EXISTS atenciones_clinicas (
    id                    BIGSERIAL PRIMARY KEY,
    cita_id               BIGINT REFERENCES citas(id),
    mascota_id            BIGINT NOT NULL REFERENCES mascotas(id),
    veterinario_id        BIGINT NOT NULL REFERENCES usuarios(id),
    triaje_id             BIGINT REFERENCES triajes(id),
    motivo_consulta       TEXT NOT NULL,
    sintomas              TEXT,
    diagnostico           TEXT NOT NULL,
    tratamiento           TEXT NOT NULL,
    observaciones_clinicas TEXT,
    creado_por            BIGINT NOT NULL REFERENCES usuarios(id),
    creado_en             TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    actualizado_por       BIGINT REFERENCES usuarios(id),
    actualizado_en        TIMESTAMPTZ
);

-- ============================================================
-- BLOQUEOS DE VETERINARIOS
-- ============================================================
CREATE TABLE IF NOT EXISTS bloqueos_veterinarios (
    id              BIGSERIAL PRIMARY KEY,
    veterinario_id  BIGINT NOT NULL REFERENCES usuarios(id),
    fecha           DATE NOT NULL,
    hora_inicio     TIME NOT NULL,
    hora_fin        TIME NOT NULL,
    motivo          VARCHAR(255)
);

-- ============================================================
-- HISTORIAL DE TRANSFERENCIAS
-- ============================================================
CREATE TABLE IF NOT EXISTS historial_transferencias (
    id                     BIGSERIAL PRIMARY KEY,
    mascota_id             BIGINT NOT NULL REFERENCES mascotas(id),
    dueno_anterior_id      BIGINT REFERENCES duenos(id),
    dueno_nuevo_id         BIGINT NOT NULL REFERENCES duenos(id),
    fecha                  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    motivo                 TEXT NOT NULL,
    usuario_responsable_id BIGINT NOT NULL REFERENCES usuarios(id)
);

-- ============================================================
-- REFRESH TOKENS
-- ============================================================
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id                BIGSERIAL PRIMARY KEY,
    usuario_id        BIGINT REFERENCES usuarios(id),
    token             VARCHAR(255) NOT NULL UNIQUE,
    fecha_expiracion  TIMESTAMPTZ NOT NULL
);

-- ============================================================
-- DATOS DE PRUEBA
-- ============================================================

TRUNCATE TABLE historial_transferencias CASCADE;
TRUNCATE TABLE bloqueos_veterinarios CASCADE;
TRUNCATE TABLE atenciones_clinicas CASCADE;
TRUNCATE TABLE sala_espera CASCADE;
TRUNCATE TABLE triajes CASCADE;
TRUNCATE TABLE citas CASCADE;
TRUNCATE TABLE disponibilidad_veterinarios CASCADE;
TRUNCATE TABLE mascota_responsables CASCADE;
TRUNCATE TABLE servicios CASCADE;
TRUNCATE TABLE contactos_emergencia CASCADE;
TRUNCATE TABLE mascotas CASCADE;
TRUNCATE TABLE duenos CASCADE;
TRUNCATE TABLE usuarios CASCADE;

-- USUARIOS
INSERT INTO usuarios (id, contrasena, nombres, apellidos, email, telefono, rol, estado, token_version) VALUES
(1, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Admin',    'PetCare',   'admin@petcare.com',      '999999001', 'ADMINISTRADOR', true, 0),
(2, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Carlos',   'Mendoza',   'carlos@petcare.com',     '999999002', 'VETERINARIO',   true, 0),
(3, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Ana',      'Gomez',     'ana@petcare.com',        '999999003', 'ASISTENTE',     true, 0),
(4, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Juan',     'Perez',     'juan.perez@gmail.com',   '999999004', 'DUENO',         true, 0),
(5, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Maria',    'Lopez',     'maria.lopez@gmail.com',  '999999005', 'DUENO',         true, 0),
(6, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Roberto',  'Garcia',    'roberto.garcia@gmail.com','999999006','DUENO',         true, 0),
(7, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Lucia',    'Fernandez', 'lucia@petcare.com',      '999999007', 'VETERINARIO',   true, 0),
(8, '$2a$10$OdbZaFPcyPOZRDZnRiXS.OiluMKtpBEXWMMTz17ODp4ITqIfpTR7G', 'Pedro',    'Ruiz',      'pedro@petcare.com',      '999999008', 'ASISTENTE',     true, 0);

-- DUENOS
INSERT INTO duenos (id, dni, telefono, direccion, usuario_id) VALUES
(1, '12345678', '999999004', 'Av. Larco 123, Miraflores',    4),
(2, '23456789', '999999005', 'Jr. Las Flores 456, San Isidro', 5),
(3, '34567890', '999999006', 'Calle Los Olivos 789, Surco',    6);

-- CONTACTOS DE EMERGENCIA
INSERT INTO contactos_emergencia (id, dueno_id, nombre, telefono, relacion) VALUES
(1, 1, 'Maria Perez',   '955555001', 'Hermana'),
(2, 2, 'Carlos Lopez',  '955555002', 'Hermano'),
(3, 3, 'Ana Garcia',    '955555003', 'Esposa');

-- MASCOTAS
INSERT INTO mascotas (id, nombre, especie, raza, genero, fecha_nacimiento, microchip, condicion_reproductiva, alergias, enfermedades_cronicas, alertas_medicas, notas_medicas, estado) VALUES
(1, 'Fido',  'Perro', 'Golden Retriever', 'MACHO', '2022-05-10', 'MC-000001', 'ESTERILIZADO', 'Penicilina', 'Ninguna',          'Cuidado con la cadera',    NULL,                  true),
(2, 'Luna',  'Gato',  'Siamés',           'HEMBRA', '2023-08-15', 'MC-000002', 'INTACTA',       NULL,          NULL,              NULL,                       'Vacunas al dia',      true),
(3, 'Rex',   'Perro', 'Pastor Alemán',    'MACHO', '2021-01-20', 'MC-000003', 'ESTERILIZADO',  NULL,         'Displasia de cadera', 'Requiere control de peso', 'Dieta especial',      true),
(4, 'Mishi', 'Gato',  'Persa',            'HEMBRA', '2022-11-30', 'MC-000004', 'ESTERILIZADO',  'Pulgas',     'Ninguna',          NULL,                       NULL,                  true),
(5, 'Copito','Conejo','Angora',           'MACHO', '2024-03-15', 'MC-000005', 'INTACTA',       NULL,          NULL,              NULL,                       NULL,                  true),
(6, 'Toby',  'Perro', 'Labrador',         'MACHO', '2020-07-22', 'MC-000006', 'ESTERILIZADO',  NULL,         'Otitis cronica',   'Revision oidos mensual',   'Usar gotas oticas',   true),
(7, 'Lola',  'Perro', 'Beagle',           'HEMBRA', '2023-12-01','MC-000007', 'INTACTA',       'Pollo',      'Ninguna',          'Propensa a obesidad',      'Dieta controlada',    true);

-- RELACION MASCOTA-DUENO
INSERT INTO mascota_responsables (id, mascota_id, dueno_id, es_principal, relacion) VALUES
(1, 1, 1, true, 'Propietario'),
(2, 2, 1, true, 'Propietario'),
(3, 3, 1, true, 'Propietario'),
(4, 4, 2, true, 'Propietario'),
(5, 5, 2, true, 'Propietario'),
(6, 6, 3, true, 'Propietario'),
(7, 7, 3, true, 'Propietario');

-- SERVICIOS
INSERT INTO servicios (id, nombre, descripcion, duracion_minutos, costo_referencial, activo) VALUES
(1, 'Consulta General', 'Revision medica general para la mascota',           30,  80.00,  true),
(2, 'Vacunacion',       'Aplicacion de vacunas segun calendario',            20,  50.00,  true),
(3, 'Desparasitacion',  'Desparasitacion interna y externa',                 15,  30.00,  true),
(4, 'Cirugia Menor',    'Procedimientos quirurgicos ambulatorios sencillos', 60,  300.00, true),
(5, 'Control',          'Cita de seguimiento para evaluar evolucion',        20,  40.00,  true),
(6, 'Emergencia',       'Atencion medica inmediata de urgencia',             45,  150.00, true);

-- DISPONIBILIDAD DE VETERINARIOS
INSERT INTO disponibilidad_veterinarios (id, veterinario_id, dia_semana, hora_inicio, hora_fin, activo) VALUES
(1, 2, 1, '09:00:00', '18:00:00', true),
(2, 2, 3, '09:00:00', '18:00:00', true),
(3, 2, 5, '09:00:00', '18:00:00', true),
(4, 7, 2, '09:00:00', '18:00:00', true),
(5, 7, 4, '09:00:00', '18:00:00', true),
(6, 7, 6, '09:00:00', '13:00:00', true);

-- CITAS
INSERT INTO citas (id, mascota_id, veterinario_id, servicio_id, fecha_hora, estado, notas, creado_por, creado_en, actualizado_en) VALUES
(1, 1, 2, 1, '2026-07-03 10:00:00-05', 'AGENDADA',  'Primera consulta general para Fido',  3, NOW(), NOW()),
(2, 2, 2, 2, '2026-07-03 11:00:00-05', 'CONFIRMADA','Vacunacion anual para Luna',          3, NOW(), NOW()),
(3, 4, 2, 3, '2026-07-03 15:00:00-05', 'CONFIRMADA','Desparasitacion para Mishi',          8, NOW(), NOW()),
(4, 6, 7, 1, '2026-07-04 09:30:00-05', 'AGENDADA',  'Consulta por otitis recurrente',      3, NOW(), NOW()),
(5, 3, 2, 5, '2026-07-06 09:30:00-05', 'AGENDADA',  'Control de peso para Rex',            3, NOW(), NOW()),
(6, 5, 2, 1, '2026-07-06 11:00:00-05', 'AGENDADA',  'Revision general para Copito',        8, NOW(), NOW()),
(7, 1, 7, 5, '2026-07-07 10:00:00-05', 'AGENDADA',  'Control de seguimiento para Fido',    3, NOW(), NOW()),
(8, 2, 2, 5, '2026-07-08 14:00:00-05', 'CONFIRMADA','Control de vacunas para Luna',         8, NOW(), NOW()),
(9, 7, 7, 1, '2026-07-09 10:00:00-05', 'AGENDADA',  'Primera consulta para Lola',          3, NOW(), NOW());

-- TRIAJES
INSERT INTO triajes (id, cita_id, motivo_visita, nivel_urgencia, signos_visibles, observaciones, peso, temperatura, frecuencia_cardiaca, frecuencia_respiratoria, asistente_id, creado_en) VALUES
(1, 1, 'Revision general y chequeo anual', 'RUTINARIA', 'Sin signos visibles', 'Mascota tranquila y cooperativa', 30.5, 38.5, 80, 20, 3, NOW()),
(2, 2, 'Vacunacion anual',                'RUTINARIA', 'Sin signos visibles', NULL,                             4.2,  38.2, 90, 22, 3, NOW());

-- SALA DE ESPERA
INSERT INTO sala_espera (id, cita_id, mascota_id, fecha_llegada, estado, observaciones) VALUES
(1, 1, 1, '2026-07-03 09:45:00-05', 'EN_ATENCION', 'Llego temprano'),
(2, 2, 2, '2026-07-03 10:50:00-05', 'PENDIENTE',   NULL);

-- ATENCIONES CLINICAS
INSERT INTO atenciones_clinicas (id, cita_id, mascota_id, veterinario_id, triaje_id, motivo_consulta, sintomas, diagnostico, tratamiento, observaciones_clinicas, creado_por, creado_en) VALUES
(1, 1, 1, 2, 1, 'Chequeo anual', 'Ninguno', 'Paciente sano, sin hallazgos anormales', 'Mantener alimentacion actual y ejercicio diario. Proxima visita en 6 meses.', 'Peso y temperatura normales. Vacunas al dia.', 2, NOW());

-- BLOQUEOS DE VETERINARIOS
INSERT INTO bloqueos_veterinarios (id, veterinario_id, fecha, hora_inicio, hora_fin, motivo) VALUES
(1, 2, '2026-07-10', '14:00:00', '16:00:00', 'Capacitacion interna');

-- HISTORIAL DE TRANSFERENCIAS
INSERT INTO historial_transferencias (id, mascota_id, dueno_nuevo_id, fecha, motivo, usuario_responsable_id) VALUES
(1, 1, 1, NOW(), 'Registro inicial de mascota', 1),
(2, 2, 1, NOW(), 'Registro inicial de mascota', 1),
(3, 3, 1, NOW(), 'Registro inicial de mascota', 1),
(4, 4, 2, NOW(), 'Registro inicial de mascota', 1),
(5, 5, 2, NOW(), 'Registro inicial de mascota', 1),
(6, 6, 3, NOW(), 'Registro inicial de mascota', 1),
(7, 7, 3, NOW(), 'Registro inicial de mascota', 1);
