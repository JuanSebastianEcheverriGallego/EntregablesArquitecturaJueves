package apicalendario.core.interfaces.servicios;

import java.util.List;

import apicalendario.dominio.entidades.Calendario;

public interface ICalendarioServicio {

    /** Genera y guarda la clasificación de todos los días del año */
    public boolean generar(int anio);

    public List<Calendario> listar(int anio);
}
