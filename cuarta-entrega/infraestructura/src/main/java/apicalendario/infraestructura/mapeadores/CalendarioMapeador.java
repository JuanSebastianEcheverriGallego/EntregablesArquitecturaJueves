package apicalendario.infraestructura.mapeadores;

import apicalendario.dominio.entidades.Calendario;
import apicalendario.infraestructura.entidades.CalendarioEntidad;
import apicalendario.infraestructura.entidades.TipoEntidad;

/** Convierte entre la entidad del dominio Calendario y la entidad JPA CalendarioEntidad */
public class CalendarioMapeador {

    private CalendarioMapeador() {
    }

    public static Calendario aDominio(CalendarioEntidad entidad) {
        if (entidad == null) {
            return null;
        }
        return new Calendario(entidad.getId(), entidad.getFecha(),
                TipoMapeador.aDominio(entidad.getTipo()), entidad.getDescripcion());
    }

    /** El tipo se recibe como entidad JPA porque debe ser un registro ya existente en la base de datos */
    public static CalendarioEntidad aEntidad(Calendario calendario, TipoEntidad tipo) {
        if (calendario == null) {
            return null;
        }
        return new CalendarioEntidad(calendario.getId() == 0 ? null : calendario.getId(),
                calendario.getFecha(), tipo, calendario.getDescripcion());
    }
}
