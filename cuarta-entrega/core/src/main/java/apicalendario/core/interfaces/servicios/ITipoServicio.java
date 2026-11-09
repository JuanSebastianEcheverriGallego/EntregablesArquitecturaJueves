package apicalendario.core.interfaces.servicios;

import java.util.List;

import apicalendario.dominio.entidades.Tipo;

public interface ITipoServicio {

    public List<Tipo> listar();

    public Tipo obtener(int id);

    public List<Tipo> buscar(String nombre);

    public Tipo agregar(Tipo tipo);

    public Tipo modificar(Tipo tipo);

    public boolean eliminar(int id);
}
