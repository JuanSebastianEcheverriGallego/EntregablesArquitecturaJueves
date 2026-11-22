package apicalendario.core.excepciones;

/** Se lanza cuando los datos recibidos no cumplen las reglas de negocio */
public class DatosNoValidosExcepcion extends RuntimeException {

    public DatosNoValidosExcepcion(String mensaje) {
        super(mensaje);
    }
}
