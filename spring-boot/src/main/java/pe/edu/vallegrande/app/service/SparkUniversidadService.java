package pe.edu.vallegrande.app.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio Spark que conecta tres bases de datos via JDBC y realiza un JOIN triple:
 *
 *   MySQL (estudiantes)  ──carrera_id = carreras.id──►  SQL Server (carreras)
 *   MySQL (estudiantes)  ──id = matriculas.estudiante_id──►  Neon/PostgreSQL (matriculas)
 */
@Service
public class SparkUniversidadService {

    private final SparkSession spark;

    // MySQL
    private final String mysqlUrl;
    private final String mysqlUsername;
    private final String mysqlPassword;
    private final String mysqlDriver;

    // SQL Server
    private final String sqlServerUrl;
    private final String sqlServerUsername;
    private final String sqlServerPassword;
    private final String sqlServerDriver;

    // Neon (PostgreSQL cloud)
    private final String neonUrl;
    private final String neonUsername;
    private final String neonPassword;
    private final String neonDriver;

    public SparkUniversidadService(
            @Value("${app.mysql.url}")      String mysqlUrl,
            @Value("${app.mysql.username}") String mysqlUsername,
            @Value("${app.mysql.password}") String mysqlPassword,
            @Value("${app.mysql.driver}")   String mysqlDriver,

            @Value("${app.sqlserver.url}")      String sqlServerUrl,
            @Value("${app.sqlserver.username}") String sqlServerUsername,
            @Value("${app.sqlserver.password}") String sqlServerPassword,
            @Value("${app.sqlserver.driver}")   String sqlServerDriver,

            @Value("${app.neon.url}")      String neonUrl,
            @Value("${app.neon.username}") String neonUsername,
            @Value("${app.neon.password}") String neonPassword,
            @Value("${app.neon.driver}")   String neonDriver
    ) {
        this.mysqlUrl      = mysqlUrl;
        this.mysqlUsername = mysqlUsername;
        this.mysqlPassword = mysqlPassword;
        this.mysqlDriver   = mysqlDriver;

        this.sqlServerUrl      = sqlServerUrl;
        this.sqlServerUsername = sqlServerUsername;
        this.sqlServerPassword = sqlServerPassword;
        this.sqlServerDriver   = sqlServerDriver;

        this.neonUrl      = neonUrl;
        this.neonUsername = neonUsername;
        this.neonPassword = neonPassword;
        this.neonDriver   = neonDriver;

        this.spark = SparkSession.builder()
                .appName("UniversidadSparkDemo")
                .master("local[*]")
                .config("spark.ui.enabled", "false")
                .config("spark.hadoop.fs.defaultFS", "file:///")
                .getOrCreate();
    }

    /**
     * JOIN triple:
     *   estudiantes (MySQL)
     *     JOIN carreras    (SQL Server)  ON estudiantes.carrera_id = carreras.id
     *     JOIN matriculas  (Neon)        ON estudiantes.id          = matriculas.estudiante_id
     *
     * Resultado: codigo, estudiante, apellido, carrera, universidad, curso, semestre, nota
     */
    public Dataset<Row> obtenerResumen() {

        Dataset<Row> estudiantes = leerEstudiantes();   // MySQL
        Dataset<Row> carreras    = leerCarreras();      // SQL Server
        Dataset<Row> matriculas  = leerMatriculas();    // Neon

        return estudiantes
                // JOIN con carreras (SQL Server)
                .join(carreras,
                        estudiantes.col("carrera_id").equalTo(carreras.col("id")))
                // JOIN con matriculas (Neon)
                .join(matriculas,
                        estudiantes.col("id").equalTo(matriculas.col("estudiante_id")))
                .select(
                        estudiantes.col("codigo"),
                        estudiantes.col("nombre").as("estudiante"),
                        estudiantes.col("apellido"),
                        carreras.col("nombre").as("carrera"),
                        carreras.col("facultad"),
                        carreras.col("universidad"),
                        matriculas.col("curso"),
                        matriculas.col("semestre"),
                        matriculas.col("nota")
                );
    }

    // ── Lectura MySQL → estudiantes ─────────────────────────────────────────
    private Dataset<Row> leerEstudiantes() {
        return spark.read()
                .format("jdbc")
                .option("url",      mysqlUrl)
                .option("dbtable",  "estudiantes")
                .option("user",     mysqlUsername)
                .option("password", mysqlPassword)
                .option("driver",   mysqlDriver)
                .load();
    }

    // ── Lectura SQL Server → carreras ────────────────────────────────────────
    private Dataset<Row> leerCarreras() {
        return spark.read()
                .format("jdbc")
                .option("url",      sqlServerUrl)
                .option("dbtable",  "dbo.carreras")
                .option("user",     sqlServerUsername)
                .option("password", sqlServerPassword)
                .option("driver",   sqlServerDriver)
                .load();
    }

    // ── Lectura Neon (PostgreSQL cloud) → matriculas ─────────────────────────
    private Dataset<Row> leerMatriculas() {
        return spark.read()
                .format("jdbc")
                .option("url",      neonUrl)
                .option("dbtable",  "public.matriculas")
                .option("user",     neonUsername)
                .option("password", neonPassword)
                .option("driver",   neonDriver)
                .load();
    }
}
