package apicalendario.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import apicalendario.core.interfaces.integracion.IFestivoServicioExterno;
import apicalendario.core.interfaces.repositorios.ICalendarioRepositorio;
import apicalendario.core.interfaces.repositorios.ITipoRepositorio;
import apicalendario.dominio.DTOs.FestivoDto;
import apicalendario.dominio.entidades.Calendario;
import apicalendario.dominio.entidades.Tipo;

/**
 * Pruebas de la lógica de generación del calendario. Los repositorios y la API Festivos
 * se reemplazan por implementaciones en memoria de las interfaces del core.
 */
class CalendarioServicioTest {

    private CalendarioEnMemoria calendarioRepositorio;
    private FestivosFalsos festivoServicio;
    private CalendarioServicio servicio;

    @BeforeEach
    void preparar() {
        calendarioRepositorio = new CalendarioEnMemoria();
        festivoServicio = new FestivosFalsos();
        servicio = new CalendarioServicio(calendarioRepositorio, new TiposEnMemoria(), festivoServicio);
    }

    @Test
    void generaTodosLosDiasDelAnio() {
        assertTrue(servicio.generar(2023));
        assertEquals(365, servicio.listar(2023).size());

        assertTrue(servicio.generar(2024));
        assertEquals(366, servicio.listar(2024).size());
    }

    @Test
    void consultaLosFestivosUnaSolaVezPorAnio() {
        servicio.generar(2023);
        assertEquals(1, festivoServicio.llamadas);
    }

    @Test
    void clasificaCadaDia() {
        festivoServicio.festivos = List.of(
                new FestivoDto("Año nuevo", LocalDate.of(2023, 1, 1)),
                new FestivoDto("Santos Reyes", LocalDate.of(2023, 1, 9)));

        servicio.generar(2023);

        // Domingo festivo: el festivo tiene prioridad sobre el fin de semana
        verificar(LocalDate.of(2023, 1, 1), CalendarioServicio.DIA_FESTIVO, "Domingo");
        verificar(LocalDate.of(2023, 1, 2), CalendarioServicio.DIA_LABORAL, "Lunes");
        verificar(LocalDate.of(2023, 1, 4), CalendarioServicio.DIA_LABORAL, "Miércoles");
        verificar(LocalDate.of(2023, 1, 7), CalendarioServicio.FIN_DE_SEMANA, "Sábado");
        verificar(LocalDate.of(2023, 1, 8), CalendarioServicio.FIN_DE_SEMANA, "Domingo");
        // Lunes festivo
        verificar(LocalDate.of(2023, 1, 9), CalendarioServicio.DIA_FESTIVO, "Lunes");
        verificar(LocalDate.of(2023, 12, 31), CalendarioServicio.FIN_DE_SEMANA, "Domingo");
    }

    @Test
    void generarDosVecesNoDuplicaDias() {
        servicio.generar(2023);
        servicio.generar(2023);
        assertEquals(365, servicio.listar(2023).size());
    }

    @Test
    void siLaApiFestivosFallaNoBorraElCalendarioExistente() {
        servicio.generar(2023);
        festivoServicio.fallar = true;

        assertThrows(IllegalStateException.class, () -> servicio.generar(2023));
        assertEquals(365, servicio.listar(2023).size());
    }

    @Test
    void listarUnAnioNoGeneradoRetornaListaVacia() {
        assertTrue(servicio.listar(2030).isEmpty());
    }

    private void verificar(LocalDate fecha, int idTipo, String descripcion) {
        Calendario dia = servicio.listar(fecha.getYear()).stream()
                .filter(c -> c.getFecha().equals(fecha))
                .findFirst()
                .orElseThrow();
        assertEquals(idTipo, dia.getTipo().getId(), "Tipo del " + fecha);
        assertEquals(descripcion, dia.getDescripcion(), "Descripción del " + fecha);
    }

    // --- Implementaciones en memoria de las interfaces del core ---

    static class TiposEnMemoria implements ITipoRepositorio {
        private final List<Tipo> tipos = new ArrayList<>(List.of(
                new Tipo(1, "Día laboral"), new Tipo(2, "Fin de semana"), new Tipo(3, "Día festivo")));

        public List<Tipo> listar() {
            return tipos;
        }

        public Tipo obtener(int id) {
            return tipos.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
        }

        public List<Tipo> buscar(String nombre) {
            return tipos.stream().filter(t -> t.getTipo().contains(nombre)).toList();
        }

        public Tipo guardar(Tipo tipo) {
            tipos.add(tipo);
            return tipo;
        }

        public boolean eliminar(int id) {
            return tipos.removeIf(t -> t.getId() == id);
        }
    }

    static class CalendarioEnMemoria implements ICalendarioRepositorio {
        private final List<Calendario> dias = new ArrayList<>();

        public List<Calendario> listar(int anio) {
            return dias.stream()
                    .filter(c -> c.getFecha().getYear() == anio)
                    .sorted(Comparator.comparing(Calendario::getFecha))
                    .toList();
        }

        public Calendario obtener(int id) {
            return dias.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
        }

        public Calendario guardar(Calendario calendario) {
            if (dias.stream().anyMatch(c -> c.getFecha().equals(calendario.getFecha()))) {
                throw new IllegalStateException("Fecha repetida: " + calendario.getFecha());
            }
            calendario.setId(dias.size() + 1);
            dias.add(calendario);
            return calendario;
        }

        public void eliminar(int anio) {
            dias.removeIf(c -> c.getFecha().getYear() == anio);
        }
    }

    static class FestivosFalsos implements IFestivoServicioExterno {
        List<FestivoDto> festivos = List.of();
        boolean fallar;
        int llamadas;

        public List<FestivoDto> obtenerFestivos(int anio) {
            llamadas++;
            if (fallar) {
                throw new IllegalStateException("La API Festivos no responde");
            }
            return festivos;
        }
    }
}
