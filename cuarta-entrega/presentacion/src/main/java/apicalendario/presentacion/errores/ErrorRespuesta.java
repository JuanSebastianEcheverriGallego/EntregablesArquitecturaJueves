package apicalendario.presentacion.errores;

/** Estructura común de las respuestas de error */
public class ErrorRespuesta {

    private int estado;
    private String mensaje;

    public ErrorRespuesta(int estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    public int getEstado() {
        return estado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
