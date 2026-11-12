package apicalendario.infraestructura.repositorios;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import apicalendario.core.interfaces.repositorios.ITipoRepositorio;
import apicalendario.dominio.entidades.Tipo;
import apicalendario.infraestructura.mapeadores.TipoMapeador;

/** Implementa el repositorio del core con Spring Data JPA y entrega objetos del dominio */
@Repository
public class TipoRepositorio implements ITipoRepositorio {

    private final ITipoRepositorioJpa repositorioJpa;

    public TipoRepositorio(ITipoRepositorioJpa repositorioJpa) {
        this.repositorioJpa = repositorioJpa;
    }

    @Override
    public List<Tipo> listar() {
        return repositorioJpa.findAll(Sort.by("id")).stream()
                .map(TipoMapeador::aDominio)
                .toList();
    }

    @Override
    public Tipo obtener(int id) {
        return repositorioJpa.findById(id)
                .map(TipoMapeador::aDominio)
                .orElse(null);
    }

    @Override
    public List<Tipo> buscar(String nombre) {
        return repositorioJpa.findByTipoContainingIgnoreCaseOrderById(nombre).stream()
                .map(TipoMapeador::aDominio)
                .toList();
    }

    @Override
    public Tipo guardar(Tipo tipo) {
        return TipoMapeador.aDominio(repositorioJpa.save(TipoMapeador.aEntidad(tipo)));
    }

    @Override
    public boolean eliminar(int id) {
        if (!repositorioJpa.existsById(id)) {
            return false;
        }
        // Si el tipo está en uso en el calendario, la llave foránea impide borrarlo
        // y se lanza DataIntegrityViolationException
        repositorioJpa.deleteById(id);
        return true;
    }
}
