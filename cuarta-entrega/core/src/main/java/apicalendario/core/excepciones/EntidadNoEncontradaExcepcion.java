package apicalendario.core.excepciones;

/** Se lanza cuando se consulta, modifica o elimina un registro que no existe */
public class EntidadNoEncontradaExcepcion extends RuntimeException {

    public EntidadNoEncontradaExcepcion(String mensaje) {
        super(mensaje);
    }
}
