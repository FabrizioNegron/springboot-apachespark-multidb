-- ============================================================
-- Base de datos: PostgreSQL en Neon
-- Tabla: matriculas
-- Relaciona con MySQL → estudiantes  via matriculas.estudiante_id = estudiantes.id
-- ============================================================
-- Ejecutar directamente en el SQL Editor de Neon Console
-- ============================================================

CREATE TABLE IF NOT EXISTS matriculas (
    id             SERIAL       PRIMARY KEY,
    estudiante_id  INT          NOT NULL,   -- FK lógica → MySQL estudiantes.id
    curso          VARCHAR(100) NOT NULL,
    semestre       VARCHAR(20)  NOT NULL,
    nota           DECIMAL(4,2) NOT NULL
);

INSERT INTO matriculas (estudiante_id, curso, semestre, nota) VALUES
    (1, 'Programación Java',      '2024-I',  18.00),
    (1, 'Bases de Datos',         '2024-I',  16.50),
    (2, 'Ingeniería de Software', '2024-I',  15.00),
    (2, 'Docker y Contenedores',  '2024-I',  17.00),
    (3, 'Programación Java',      '2024-I',  14.00),
    (3, 'Apache Spark',           '2024-I',  18.50),
    (4, 'Redes y Comunicaciones', '2024-I',  19.00),
    (5, 'Ingeniería de Software', '2024-II', 16.00),
    (6, 'Estructuras de Datos',   '2024-II', 13.50);

SELECT * FROM matriculas;
