package apicalendario.infraestructura.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA de la tabla tipo. Solo se usa dentro de la infraestructura:
 * hacia las demás capas viaja como objeto del dominio Tipo.
 */
@Entity
@Table(name = "tipo")
public class TipoEntidad {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo", length = 100, unique = true, nullable = false)
    private String tipo;

    public TipoEntidad() {
    }

    public TipoEntidad(Integer id, String tipo) {
        this.id = id;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
