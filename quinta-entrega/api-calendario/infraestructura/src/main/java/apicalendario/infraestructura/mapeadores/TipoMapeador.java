package apicalendario.infraestructura.mapeadores;

import apicalendario.dominio.entidades.Tipo;
import apicalendario.infraestructura.entidades.TipoEntidad;

/** Convierte entre la entidad del dominio Tipo y la entidad JPA TipoEntidad */
public class TipoMapeador {

    private TipoMapeador() {
    }

    public static Tipo aDominio(TipoEntidad entidad) {
        if (entidad == null) {
            return null;
        }
        return new Tipo(entidad.getId(), entidad.getTipo());
    }

    /** Un id 0 en el dominio es un tipo nuevo: en JPA va en null para que lo asigne la base de datos */
    public static TipoEntidad aEntidad(Tipo tipo) {
        if (tipo == null) {
            return null;
        }
        return new TipoEntidad(tipo.getId() == 0 ? null : tipo.getId(), tipo.getTipo());
    }
}
