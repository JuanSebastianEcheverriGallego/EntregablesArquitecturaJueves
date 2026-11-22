package apicalendario.presentacion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import apicalendario.core.interfaces.servicios.ICalendarioServicio;
import apicalendario.dominio.entidades.Calendario;

@RestController
@RequestMapping("/api/calendario")
public class CalendarioControlador {

    private static final Logger LOG = LoggerFactory.getLogger(CalendarioControlador.class);

    private final ICalendarioServicio servicio;

    public CalendarioControlador(ICalendarioServicio servicio) {
        this.servicio = servicio;
    }

    /**
     * Retorna true si el calendario se generó y guardó. Si la API Festivos no responde
     * o falla el guardado retorna false; la transacción ya se revirtió en el servicio.
     */
    @RequestMapping(value = "/generar/{anio}", method = RequestMethod.GET)
    public boolean generar(@PathVariable int anio) {
        try {
            return servicio.generar(anio);
        } catch (RuntimeException ex) {
            LOG.error("No se pudo generar el calendario del año {}", anio, ex);
            return false;
        }
    }

    /** Días del año ordenados por fecha. Lista vacía si el año no se ha generado */
    @RequestMapping(value = "/listar/{anio}", method = RequestMethod.GET)
    public List<Calendario> listar(@PathVariable int anio) {
        return servicio.listar(anio);
    }
}
