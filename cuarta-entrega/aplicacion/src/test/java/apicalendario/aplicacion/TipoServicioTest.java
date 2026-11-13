package apicalendario.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import apicalendario.core.excepciones.DatosNoValidosExcepcion;
import apicalendario.core.excepciones.EntidadNoEncontradaExcepcion;
import apicalendario.dominio.entidades.Tipo;

class TipoServicioTest {

    private TipoServicio servicio;

    @BeforeEach
    void preparar() {
        servicio = new TipoServicio(new CalendarioServicioTest.TiposEnMemoria());
    }

    @Test
    void obtenerUnTipoQueNoExisteLanzaExcepcion() {
        assertThrows(EntidadNoEncontradaExcepcion.class, () -> servicio.obtener(99));
    }

    @Test
    void agregarSinNombreLanzaExcepcion() {
        assertThrows(DatosNoValidosExcepcion.class, () -> servicio.agregar(new Tipo(0, "  ")));
        assertThrows(DatosNoValidosExcepcion.class, () -> servicio.agregar(new Tipo(0, null)));
    }

    @Test
    void agregarQuitaEspaciosDelNombre() {
        Tipo tipo = servicio.agregar(new Tipo(7, "  Día cívico "));
        assertEquals("Día cívico", tipo.getTipo());
        // El id lo asigna la base de datos, no el consumidor
        assertEquals(0, tipo.getId());
    }

    @Test
    void modificarOEliminarUnTipoQueNoExisteLanzaExcepcion() {
        assertThrows(EntidadNoEncontradaExcepcion.class, () -> servicio.modificar(new Tipo(99, "Otro")));
        assertThrows(EntidadNoEncontradaExcepcion.class, () -> servicio.eliminar(99));
    }

    @Test
    void losTiposQueUsaLaGeneracionNoSePuedenEliminar() {
        assertThrows(DatosNoValidosExcepcion.class, () -> servicio.eliminar(CalendarioServicio.DIA_LABORAL));
        assertThrows(DatosNoValidosExcepcion.class, () -> servicio.eliminar(CalendarioServicio.FIN_DE_SEMANA));
        assertThrows(DatosNoValidosExcepcion.class, () -> servicio.eliminar(CalendarioServicio.DIA_FESTIVO));
        assertEquals(3, servicio.listar().size());
    }
}
