-- Grupos de ejemplo (programa_educativo_id debe existir en microservicio-division)
INSERT INTO grupo_entity (id, nombre, programa_educativo_id, profesor_id, activo) VALUES (1, 'IDGS-12A', 1, 1, true);
INSERT INTO grupo_entity (id, nombre, programa_educativo_id, profesor_id, activo) VALUES (2, 'IDGS-12B', 1, 2, true);
INSERT INTO grupo_entity (id, nombre, programa_educativo_id, activo) VALUES (3, 'ITICS-12A', 2, true);

-- Alumnos de ejemplo
INSERT INTO alumno_entity (id, nombre, apellido, matricula, correo, telefono, programa_educativo_id, grupo_id, activo)
VALUES (1, 'Juan', 'Pérez García', '2012010001', 'juan.perez@uteq.edu.mx', '4421234567', 1, 1, true);

INSERT INTO alumno_entity (id, nombre, apellido, matricula, correo, telefono, programa_educativo_id, grupo_id, activo)
VALUES (2, 'María', 'López Hernández', '2012010002', 'maria.lopez@uteq.edu.mx', '4421234568', 1, 1, true);

INSERT INTO alumno_entity (id, nombre, apellido, matricula, correo, telefono, programa_educativo_id, grupo_id, activo)
VALUES (3, 'Carlos', 'Martínez Sánchez', '2012010003', 'carlos.martinez@uteq.edu.mx', '4421234569', 1, 2, true);

INSERT INTO alumno_entity (id, nombre, apellido, matricula, correo, telefono, programa_educativo_id, grupo_id, activo)
VALUES (4, 'Ana', 'González Ramírez', '2012010004', 'ana.gonzalez@uteq.edu.mx', '4421234570', 1, 2, true);

INSERT INTO alumno_entity (id, nombre, apellido, matricula, correo, telefono, programa_educativo_id, activo)
VALUES (5, 'Luis', 'Rodríguez Torres', '2012020001', 'luis.rodriguez@uteq.edu.mx', '4421234571', 2, true);

INSERT INTO alumno_entity (id, nombre, apellido, matricula, correo, telefono, programa_educativo_id, grupo_id, activo)
VALUES (6, 'Laura', 'Fernández Cruz', '2012010005', 'laura.fernandez@uteq.edu.mx', '4421234572', 1, 1, true);

-- Alumno inactivo para pruebas
INSERT INTO alumno_entity (id, nombre, apellido, matricula, correo, telefono, programa_educativo_id, activo)
VALUES (7, 'Pedro', 'Gómez Silva', '2012010006', 'pedro.gomez@uteq.edu.mx', '4421234573', 1, false);
