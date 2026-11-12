package apicalendario.dominio.DTOs;

import java.time.LocalDate;

/**
 * Festivo que entrega la API Festivos en GET /api/festivos/obtener/{anio}.
 * No se guarda en la base de datos: solo se usa para clasificar los días del año.
 */
public class FestivoDto {

    private String festivo;
    private LocalDate fecha;

    public FestivoDto() {
    }

    public FestivoDto(String festivo, LocalDate fecha) {
        this.festivo = festivo;
        this.fecha = fecha;
    }

    public String getFestivo() {
        return festivo;
    }

    public void setFestivo(String festivo) {
        this.festivo = festivo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
