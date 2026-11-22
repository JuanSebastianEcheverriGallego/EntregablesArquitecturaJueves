package apicalendario.aplicacion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apicalendario.core.interfaces.integracion.IFestivoServicioExterno;
import apicalendario.core.interfaces.repositorios.ICalendarioRepositorio;
import apicalendario.core.interfaces.repositorios.ITipoRepositorio;
import apicalendario.core.interfaces.servicios.ICalendarioServicio;
import apicalendario.dominio.DTOs.FestivoDto;
import apicalendario.dominio.entidades.Calendario;
import apicalendario.dominio.entidades.Tipo;

/**
 * Genera y lista el calendario laboral de un año.
 */
@Service
public class CalendarioServicio implements ICalendarioServicio {

    // Ids de los tipos del catálogo (ver bd/DML_Calendario.sql)
    public static final int DIA_LABORAL = 1;
    public static final int FIN_DE_SEMANA = 2;
    public static final int DIA_FESTIVO = 3;

    private final ICalendarioRepositorio calendarioRepositorio;
    private final ITipoRepositorio tipoRepositorio;
    private final IFestivoServicioExterno festivoServicio;

    public CalendarioServicio(ICalendarioRepositorio calendarioRepositorio,
            ITipoRepositorio tipoRepositorio, IFestivoServicioExterno festivoServicio) {
        this.calendarioRepositorio = calendarioRepositorio;
        this.tipoRepositorio = tipoRepositorio;
        this.festivoServicio = festivoServicio;
    }

    /**
     * Pide una sola vez a la API Festivos los festivos del año y los deja en memoria,
     * en lugar de consultar fecha por fecha (365 llamadas).
     * El borrado del año y el guardado de los días nuevos van en una sola transacción:
     * si algo falla, el calendario que existía no se pierde. Los errores no se capturan
     * aquí para que la transacción se revierta; el controlador los convierte en false.
     */
    @Override
    @Transactional
    public boolean generar(int anio) {
        Set<LocalDate> festivos = festivoServicio.obtenerFestivos(anio).stream()
                .map(FestivoDto::getFecha)
                .collect(Collectors.toSet());

        Tipo laboral = obtenerTipo(DIA_LABORAL);
        Tipo finDeSemana = obtenerTipo(FIN_DE_SEMANA);
        Tipo festivo = obtenerTipo(DIA_FESTIVO);

        calendarioRepositorio.eliminar(anio);

        for (LocalDate fecha = LocalDate.of(anio, 1, 1); fecha.getYear() == anio; fecha = fecha.plusDays(1)) {
            Tipo tipo;
            // Un festivo tiene prioridad sobre el fin de semana
            if (festivos.contains(fecha)) {
                tipo = festivo;
            } else if (esFinDeSemana(fecha)) {
                tipo = finDeSemana;
            } else {
                tipo = laboral;
            }
            calendarioRepositorio.guardar(new Calendario(0, fecha, tipo, nombreDia(fecha.getDayOfWeek())));
        }
        return true;
    }

    @Override
    public List<Calendario> listar(int anio) {
        return calendarioRepositorio.listar(anio);
    }

    private Tipo obtenerTipo(int id) {
        Tipo tipo = tipoRepositorio.obtener(id);
        if (tipo == null) {
            throw new IllegalStateException("No existe en el catálogo el tipo de día con id=" + id);
        }
        return tipo;
    }

    private static boolean esFinDeSemana(LocalDate fecha) {
        DayOfWeek dia = fecha.getDayOfWeek();
        return dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY;
    }

    /** Nombre del día en español, sin depender del idioma configurado en el servidor */
    static String nombreDia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miércoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}
