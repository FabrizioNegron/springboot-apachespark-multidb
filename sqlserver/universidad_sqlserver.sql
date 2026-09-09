-- ============================================================
-- Base de datos: universidad_sqlserver  (SQL Server)
-- Tabla: carreras
-- Relaciona con MySQL → estudiantes  via estudiantes.carrera_id = carreras.id
-- ============================================================

CREATE DATABASE universidad_sqlserver;
GO

USE universidad_sqlserver;
GO

CREATE TABLE carreras (
    id          INT IDENTITY(1,1) PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    facultad    VARCHAR(100) NOT NULL,
    universidad VARCHAR(150) NOT NULL
);
GO

INSERT INTO carreras (nombre, facultad, universidad) VALUES
    ('Ingeniería de Sistemas',  'Facultad de Ingeniería',    'Universidad Valle Grande'),
    ('Ingeniería de Software',  'Facultad de Ingeniería',    'Universidad Valle Grande'),
    ('Ingeniería de Redes',     'Facultad de Ingeniería',    'Universidad Valle Grande'),
    ('Ingeniería Civil',        'Facultad de Ingeniería',    'Universidad Valle Grande');
GO

SELECT * FROM carreras;
GO
