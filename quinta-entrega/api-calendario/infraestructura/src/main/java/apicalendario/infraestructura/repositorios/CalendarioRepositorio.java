package apicalendario.infraestructura.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import apicalendario.core.interfaces.repositorios.ICalendarioRepositorio;
import apicalendario.dominio.entidades.Calendario;
import apicalendario.infraestructura.entidades.TipoEntidad;
import apicalendario.infraestructura.mapeadores.CalendarioMapeador;

/** Implementa el repositorio del core con Spring Data JPA y entrega objetos del dominio */
@Repository
public class CalendarioRepositorio implements ICalendarioRepositorio {

    private final ICalendarioRepositorioJpa repositorioJpa;
    private final ITipoRepositorioJpa tipoRepositorioJpa;

    public CalendarioRepositorio(ICalendarioRepositorioJpa repositorioJpa,
            ITipoRepositorioJpa tipoRepositorioJpa) {
        this.repositorioJpa = repositorioJpa;
        this.tipoRepositorioJpa = tipoRepositorioJpa;
    }

    @Override
    public List<Calendario> listar(int anio) {
        return repositorioJpa.listarPorPeriodo(LocalDate.of(anio, 1, 1), LocalDate.of(anio, 12, 31))
                .stream()
                .map(CalendarioMapeador::aDominio)
                .toList();
    }

    @Override
    public Calendario obtener(int id) {
        return repositorioJpa.findById(id)
                .map(CalendarioMapeador::aDominio)
                .orElse(null);
    }

    @Override
    public Calendario guardar(Calendario calendario) {
        // Referencia al tipo existente por su id, sin consultarlo de nuevo
        TipoEntidad tipo = tipoRepositorioJpa.getReferenceById(calendario.getTipo().getId());
        return CalendarioMapeador.aDominio(
                repositorioJpa.save(CalendarioMapeador.aEntidad(calendario, tipo)));
    }

    @Override
    public void eliminar(int anio) {
        repositorioJpa.eliminarPorPeriodo(LocalDate.of(anio, 1, 1), LocalDate.of(anio, 12, 31));
    }
}
