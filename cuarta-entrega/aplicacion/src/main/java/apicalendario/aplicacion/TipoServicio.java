package apicalendario.aplicacion;

import java.util.List;

import org.springframework.stereotype.Service;

import apicalendario.core.excepciones.DatosNoValidosExcepcion;
import apicalendario.core.excepciones.EntidadNoEncontradaExcepcion;
import apicalendario.core.interfaces.repositorios.ITipoRepositorio;
import apicalendario.core.interfaces.servicios.ITipoServicio;
import apicalendario.dominio.entidades.Tipo;

/**
 * CRUD del catálogo de tipos de día, para administrarlo desde la API
 * sin modificar directamente la base de datos.
 */
@Service
public class TipoServicio implements ITipoServicio {

    private static final int LONGITUD_MAXIMA = 100;

    private final ITipoRepositorio repositorio;

    public TipoServicio(ITipoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Tipo> listar() {
        return repositorio.listar();
    }

    @Override
    public Tipo obtener(int id) {
        Tipo tipo = repositorio.obtener(id);
        if (tipo == null) {
            throw new EntidadNoEncontradaExcepcion("No existe el tipo con id=" + id);
        }
        return tipo;
    }

    @Override
    public List<Tipo> buscar(String nombre) {
        return repositorio.buscar(nombre.trim());
    }

    @Override
    public Tipo agregar(Tipo tipo) {
        validar(tipo);
        // El id lo asigna la base de datos
        tipo.setId(0);
        return repositorio.guardar(tipo);
    }

    @Override
    public Tipo modificar(Tipo tipo) {
        validar(tipo);
        obtener(tipo.getId());
        return repositorio.guardar(tipo);
    }

    @Override
    public boolean eliminar(int id) {
        obtener(id);
        return repositorio.eliminar(id);
    }

    private void validar(Tipo tipo) {
        if (tipo == null || tipo.getTipo() == null || tipo.getTipo().isBlank()) {
            throw new DatosNoValidosExcepcion("El nombre del tipo es obligatorio");
        }
        tipo.setTipo(tipo.getTipo().trim());
        if (tipo.getTipo().length() > LONGITUD_MAXIMA) {
            throw new DatosNoValidosExcepcion(
                    "El nombre del tipo no puede superar " + LONGITUD_MAXIMA + " caracteres");
        }
    }
}
