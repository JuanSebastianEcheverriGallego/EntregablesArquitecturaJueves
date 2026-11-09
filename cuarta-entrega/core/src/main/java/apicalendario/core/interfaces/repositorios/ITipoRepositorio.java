package apicalendario.core.interfaces.repositorios;

import java.util.List;

import apicalendario.dominio.entidades.Tipo;

public interface ITipoRepositorio {

    public List<Tipo> listar();

    /** Retorna null si el tipo no existe */
    public Tipo obtener(int id);

    /** Tipos cuyo nombre contiene el texto, sin distinguir mayúsculas */
    public List<Tipo> buscar(String nombre);

    /** Agrega el tipo si su id es 0, o lo modifica si ya existe */
    public Tipo guardar(Tipo tipo);

    public boolean eliminar(int id);
}
