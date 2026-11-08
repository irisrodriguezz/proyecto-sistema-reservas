-- =====================================================================
-- Sistema de Gestión de Reservas · Script de creación de la base de datos
-- Ejecutar con: mysql -u TU_USUARIO -p -P 3307 < database/schema.sql
--
-- Las tablas son idénticas a las que crea la propia aplicación al arrancar
-- (DBConnection.inicializarEsquema), así que el resultado es el mismo
-- se ejecute antes o después este script.
-- ATENCIÓN: borra la base de datos si ya existe.
-- =====================================================================

DROP DATABASE IF EXISTS base_de_datos_mvc;

CREATE DATABASE base_de_datos_mvc
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish2_ci;

USE base_de_datos_mvc;

-- ---------------------------------------------------------------------
-- Tablas
-- ---------------------------------------------------------------------

CREATE TABLE USUARIO (
    id_usuario INT NOT NULL AUTO_INCREMENT,
    correo_electronico VARCHAR(100) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    fecha_de_nacimiento DATE,
    tipo_usuario ENUM('Administrador', 'Normal') NOT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY (correo_electronico)
);

CREATE TABLE RECURSO (
    id_recurso INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    ubicacion VARCHAR(200),
    capacidad INT
);

CREATE TABLE HORARIO (
    id_horario INT PRIMARY KEY,
    dia_semana ENUM('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    CONSTRAINT chk_hora CHECK (hora_fin > hora_inicio)
);

-- El identificador se hereda de USUARIO, por eso no es AUTO_INCREMENT
CREATE TABLE ADMINISTRADOR (
    id_usuario INT PRIMARY KEY,
    telefono_guardia VARCHAR(20) NOT NULL,
    CONSTRAINT fk_administrador_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
);

-- El identificador se hereda de USUARIO, por eso no es AUTO_INCREMENT
CREATE TABLE USUARIONORMAL (
    id_usuario INT PRIMARY KEY,
    direccion VARCHAR(255),
    telefono_movil VARCHAR(20),
    fotografia VARCHAR(255),
    CONSTRAINT fk_normal_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE DISPONIBLEEN (
    id_recurso INT NOT NULL,
    id_horario INT NOT NULL,
    PRIMARY KEY (id_recurso, id_horario),
    CONSTRAINT fk_disponibleen_recurso FOREIGN KEY (id_recurso)
        REFERENCES RECURSO(id_recurso) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_disponibleen_horario FOREIGN KEY (id_horario)
        REFERENCES HORARIO(id_horario) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE RESERVA (
    id_recurso INT NOT NULL,
    id_reserva_local INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    coste DOUBLE,
    numero_plazas INT,
    motivo VARCHAR(200),
    observaciones TEXT,
    PRIMARY KEY (id_recurso, id_reserva_local),
    CONSTRAINT fk_reserva_recurso FOREIGN KEY (id_recurso)
        REFERENCES RECURSO(id_recurso) ON DELETE CASCADE,
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (id_usuario)
        REFERENCES USUARIO(id_usuario) ON DELETE CASCADE
);

-- ---------------------------------------------------------------------
-- Datos de ejemplo (ficticios; las contraseñas son marcadores de posición)
-- ---------------------------------------------------------------------

INSERT INTO USUARIO (correo_electronico, contrasena, nombre, fecha_de_nacimiento, tipo_usuario) VALUES
('admin1@empresa.com', 'hash_admin1', 'Admin Principal', '1994-09-25', 'Administrador'),
('admin2@empresa.com', 'hash_admin2', 'Admin Empresa', '1985-06-15', 'Administrador'),
('correo.electronico1@gmail.com', 'hash_normal1', 'María Díaz Castro', '1981-01-09', 'Normal'),
('correo.electronico2@gmail.com', 'hash_normal2', 'Pedro Coves Torres', '1998-12-21', 'Normal'),
('correo.electronico3@gmail.com', 'hash_normal3', 'Alejandro Torres Vargas', '2000-03-24', 'Normal');

-- Los ids 1 y 2 corresponden a los administradores de USUARIO
INSERT INTO ADMINISTRADOR (id_usuario, telefono_guardia) VALUES
(1, '123456789'),
(2, '687654321');

-- Los ids 3, 4 y 5 corresponden a los usuarios normales de USUARIO
INSERT INTO USUARIONORMAL (id_usuario, direccion, telefono_movil, fotografia) VALUES
(3, 'Avenida Inventada 20 1, Elche', '662843154', 'url/foto_maria.png'),
(4, NULL, '651348795', 'url/foto_pedro.png'),
(5, 'Avenida Constitucion 2 4, Santa Pola', '651348795', NULL);

INSERT INTO RECURSO (nombre, descripcion, ubicacion, capacidad) VALUES
('Sala de Proyecciones 1', 'Sala equipada con proyectores y asientos', 'Torre A, Planta 3', 30),
('Salón de Eventos 3', 'Salón diseñado para albergar reuniones, exhibiciones, convenciones...', 'Torre B, Planta Baja', 500),
('Multiespacio', 'Espacio con capacidad para alojar múltiples actividades', 'Torre C, Planta Baja', 2000),
('Sala de Reuniones 4', 'Lugar ideal para realizar reuniones y asambleas', 'Torre A, Planta 2', 20),
('Zona de Descanso', 'Espacio para relajarse con sillones y máquinas de café', 'Torre A, Planta 1', 50);

INSERT INTO HORARIO (id_horario, dia_semana, hora_inicio, hora_fin) VALUES
(10, 'Lunes', '08:00:00', '20:59:59'),
(11, 'Martes', '08:00:00', '20:59:59'),
(12, 'Miércoles', '08:00:00', '20:59:59'),
(13, 'Jueves', '10:00:00', '22:59:59'),
(14, 'Viernes', '07:00:00', '23:59:59');

INSERT INTO DISPONIBLEEN (id_recurso, id_horario) VALUES
(1, 10),
(1, 11),
(2, 14),
(3, 12),
(5, 13);

INSERT INTO RESERVA (id_recurso, id_reserva_local, id_usuario, fecha, hora_inicio, hora_fin, coste, numero_plazas, motivo, observaciones) VALUES
-- María (3) reserva la Sala de Proyecciones (1)
(1, 1, 3, '2026-02-15', '10:15:00', '11:45:00', 0.00, 20, 'Conferencia presentación de Proyecto', 'Traer mesa de otra sala'),
-- Alejandro (5) reserva la Sala de Reuniones (4)
(4, 2, 5, '2026-01-06', '11:00:00', '14:00:00', 0.00, 10, 'Reunión para establecer la temporalización del Proyecto', 'Necesario confirmar asistencia de invitados'),
-- Alejandro (5) reserva ese mismo día la Zona de Descanso (5)
(5, 2, 5, '2026-01-06', '14:30:00', '17:30:00', 100.00, 10, 'Comida de después de la reunión', 'Traer catering'),
-- Pedro (4) reserva el Salón de Eventos 3 (2)
(2, 3, 4, '2026-05-11', '09:00:00', '20:00:00', 600.00, 100, 'Exhibición final del proyecto junto a miembros de la Empresa', NULL),
-- Pedro (4) reserva el Multiespacio (3)
(3, 3, 4, '2026-05-22', '09:00:00', '23:45:00', 3000.00, 1500, 'Gran presentación oficial del proyecto', NULL);
