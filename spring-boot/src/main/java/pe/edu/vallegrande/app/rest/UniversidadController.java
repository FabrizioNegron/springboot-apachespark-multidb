package pe.edu.vallegrande.app.rest;

import pe.edu.vallegrande.app.service.SparkUniversidadService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Expone el resultado del JOIN triple:
 *   MySQL (estudiantes) + SQL Server (carreras) + Neon (matriculas)
 */
@RestController
@RequestMapping("/api/universidad")
public class UniversidadController {

    private final SparkUniversidadService sparkService;

    public UniversidadController(SparkUniversidadService sparkService) {
        this.sparkService = sparkService;
    }

    /**
     * GET /api/universidad/matriculas
     *
     * Ejecuta el JOIN Spark y devuelve información consolidada:
     *   codigo, estudiante, apellido, carrera, facultad, universidad, curso, semestre, nota
     */
    @GetMapping("/matriculas")
    public List<Map<String, Object>> obtenerMatriculas() {

        Dataset<Row> resultado = sparkService.obtenerResumen();

        return resultado.collectAsList()
                .stream()
                .map(row -> Map.of(
                        "codigo",       row.getAs("codigo"),
                        "estudiante",   row.getAs("estudiante"),
                        "apellido",     row.getAs("apellido"),
                        "carrera",      row.getAs("carrera"),
                        "facultad",     row.getAs("facultad"),
                        "universidad",  row.getAs("universidad"),
                        "curso",        row.getAs("curso"),
                        "semestre",     row.getAs("semestre"),
                        "nota",         row.getAs("nota")
                ))
                .toList();
    }
}
