package apicalendario.dominio.entidades;

import java.time.LocalDate;

/**
 * Día del calendario con su clasificación.
 * Contiene el objeto Tipo completo: la llave foránea IdTipo solo existe en la entidad JPA.
 */
public class Calendario {

    private int id;
    private LocalDate fecha;
    private Tipo tipo;
    private String descripcion;

    public Calendario() {
    }

    public Calendario(int id, LocalDate fecha, Tipo tipo, String descripcion) {
        this.id = id;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
