package apicalendario.core.interfaces.repositorios;

import java.util.List;

import apicalendario.dominio.entidades.Calendario;

public interface ICalendarioRepositorio {

    /** Días del año ordenados por fecha */
    public List<Calendario> listar(int anio);

    /** Retorna null si el día no existe */
    public Calendario obtener(int id);

    public Calendario guardar(Calendario calendario);

    /** Elimina los días del año, para que generarlo de nuevo no los duplique */
    public void eliminar(int anio);
}
