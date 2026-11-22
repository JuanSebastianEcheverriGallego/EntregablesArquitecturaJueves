package apicalendario.infraestructura.repositorios;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apicalendario.infraestructura.entidades.CalendarioEntidad;

/** Spring Data JPA implementa esta interfaz automáticamente */
@Repository
public interface ICalendarioRepositorioJpa extends JpaRepository<CalendarioEntidad, Integer> {

    // JOIN FETCH trae el tipo en la misma consulta, en lugar de una consulta por cada día
    @Query("SELECT c FROM CalendarioEntidad c JOIN FETCH c.tipo "
            + "WHERE c.fecha BETWEEN :desde AND :hasta ORDER BY c.fecha")
    public List<CalendarioEntidad> listarPorPeriodo(@Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    // Borrado en una sola sentencia DELETE, en lugar de cargar y borrar cada día
    @Modifying
    @Query("DELETE FROM CalendarioEntidad c WHERE c.fecha BETWEEN :desde AND :hasta")
    public int eliminarPorPeriodo(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
