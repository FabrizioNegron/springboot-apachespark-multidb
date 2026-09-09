-- ============================================================
-- Base de datos: universidad_mysql  (MySQL)
-- Tabla: estudiantes
-- Relaciona con SQL Server → carreras  via carrera_id
-- ============================================================

CREATE DATABASE IF NOT EXISTS universidad_mysql
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE universidad_mysql;

CREATE TABLE IF NOT EXISTS estudiantes (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    codigo     VARCHAR(20)  NOT NULL,
    nombre     VARCHAR(100) NOT NULL,
    apellido   VARCHAR(100) NOT NULL,
    carrera_id INT          NOT NULL   -- FK lógica → SQL Server carreras.id
);

INSERT INTO estudiantes (codigo, nombre, apellido, carrera_id) VALUES
    ('E001', 'Juan',   'Perez',   1),
    ('E002', 'Ana',    'Lopez',   2),
    ('E003', 'Pedro',  'Torres',  1),
    ('E004', 'Maria',  'Quispe',  3),
    ('E005', 'Carlos', 'Mamani',  2),
    ('E006', 'Lucia',  'Flores',  4);

SELECT * FROM estudiantes;
