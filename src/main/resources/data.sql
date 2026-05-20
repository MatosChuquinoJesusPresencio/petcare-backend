-- Datos iniciales de prueba para PetCare

-- 1. Usuarios (Password es 'password' en todos)
-- Hashed: $2a$10$d57RJc0QNnQ1epY.c/3fT.A.SgM52D3f93F.1o0dD6X3.7s5Q2h6G
INSERT INTO usuarios (username, password, nombre, apellido, email, telefono, rol, activo) VALUES
('admin', '$2a$10$d57RJc0QNnQ1epY.c/3fT.A.SgM52D3f93F.1o0dD6X3.7s5Q2h6G', 'Admin', 'PetCare', 'admin@petcare.com', '999999999', 'ADMINISTRADOR', true),
('veterinario1', '$2a$10$d57RJc0QNnQ1epY.c/3fT.A.SgM52D3f93F.1o0dD6X3.7s5Q2h6G', 'Carlos', 'Mendoza', 'carlos@petcare.com', '988888888', 'VETERINARIO', true),
('asistente1', '$2a$10$d57RJc0QNnQ1epY.c/3fT.A.SgM52D3f93F.1o0dD6X3.7s5Q2h6G', 'Ana', 'Gomez', 'ana@petcare.com', '977777777', 'ASISTENTE', true),
('dueno1', '$2a$10$d57RJc0QNnQ1epY.c/3fT.A.SgM52D3f93F.1o0dD6X3.7s5Q2h6G', 'Juan', 'Perez', 'juan.perez@gmail.com', '966666666', 'DUENO', true);

-- 2. Dueños
INSERT INTO duenos (nombre, apellido, dni, email, telefono, direccion, usuario_id, activo) VALUES
('Juan', 'Perez', '12345678', 'juan.perez@gmail.com', '966666666', 'Av. Larco 123, Miraflores', 4, true);

-- 3. Contactos de Emergencia
INSERT INTO contactos_emergencia (dueno_id, nombre, telefono, relacion) VALUES
(1, 'Maria Perez', '955555555', 'Hermana');

-- 4. Mascotas
INSERT INTO mascotas (nombre, especie, raza, sexo, fecha_nacimiento, microchip, condicion_reproductiva, alergias, enfermedades_cronicas, alertas_medicas, activo) VALUES
('Fido', 'Perro', 'Golden Retriever', 'MACHO', '2022-05-10', 'MC-12345', 'ESTERILIZADO', 'Penicilina', 'Ninguna', 'Cuidado con la cadera', true);

-- 5. Relación Mascota-Dueño
INSERT INTO mascota_responsables (mascota_id, dueno_id, es_principal, relacion) VALUES
(1, 1, true, 'Propietario');

-- 6. Servicios
INSERT INTO servicios (nombre, descripcion, duracion_minutos, costo_referencial, activo) VALUES
('Consulta General', 'Revisión médica general para la mascota', 30, 80.00, true),
('Vacunación', 'Aplicación de vacunas según calendario', 20, 50.00, true),
('Desparasitación', 'Desparasitación interna y externa', 15, 30.00, true),
('Cirugía Menor', 'Procedimientos quirúrgicos ambulatorios sencillos', 60, 300.00, true),
('Control', 'Cita de seguimiento para evaluar evolución', 20, 40.00, true),
('Emergencia', 'Atención médica inmediata de urgencia', 45, 150.00, true);

-- 7. Disponibilidad de Veterinarios (veterinario1 = id 2)
-- Lunes (1), Miércoles (3), Viernes (5) de 9:00 AM a 6:00 PM
INSERT INTO disponibilidad_veterinarios (veterinario_id, dia_semana, hora_inicio, hora_fin, activo) VALUES
(2, 1, '09:00:00', '18:00:00', true),
(2, 3, '09:00:00', '18:00:00', true),
(2, 5, '09:00:00', '18:00:00', true);
