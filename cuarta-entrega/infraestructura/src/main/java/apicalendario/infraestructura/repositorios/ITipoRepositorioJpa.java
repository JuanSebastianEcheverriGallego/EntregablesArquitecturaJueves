package apicalendario.infraestructura.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apicalendario.infraestructura.entidades.TipoEntidad;

/** Spring Data JPA implementa esta interfaz automáticamente */
@Repository
public interface ITipoRepositorioJpa extends JpaRepository<TipoEntidad, Integer> {

    public List<TipoEntidad> findByTipoContainingIgnoreCaseOrderById(String tipo);
}
