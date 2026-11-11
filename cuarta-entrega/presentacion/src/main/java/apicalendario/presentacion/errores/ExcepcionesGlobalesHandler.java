package apicalendario.presentacion.errores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import apicalendario.core.excepciones.DatosNoValidosExcepcion;
import apicalendario.core.excepciones.EntidadNoEncontradaExcepcion;

/** Convierte las excepciones de los controladores en respuestas ErrorRespuesta */
@RestControllerAdvice
public class ExcepcionesGlobalesHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ExcepcionesGlobalesHandler.class);

    // Por ejemplo /api/calendario/listar/abc
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorRespuesta> parametroNoValido(MethodArgumentTypeMismatchException ex) {
        return responder(HttpStatus.BAD_REQUEST, "El parámetro '" + ex.getName() + "' debe ser un número entero");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> cuerpoNoValido(HttpMessageNotReadableException ex) {
        return responder(HttpStatus.BAD_REQUEST, "El cuerpo de la solicitud no es un JSON válido");
    }

    @ExceptionHandler(DatosNoValidosExcepcion.class)
    public ResponseEntity<ErrorRespuesta> datosNoValidos(DatosNoValidosExcepcion ex) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntidadNoEncontradaExcepcion.class)
    public ResponseEntity<ErrorRespuesta> noEncontrado(EntidadNoEncontradaExcepcion ex) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorRespuesta> rutaNoExiste(NoResourceFoundException ex) {
        return responder(HttpStatus.NOT_FOUND, "La ruta /" + ex.getResourcePath() + " no existe");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorRespuesta> metodoNoPermitido(HttpRequestMethodNotSupportedException ex) {
        return responder(HttpStatus.METHOD_NOT_ALLOWED, "El método " + ex.getMethod() + " no está permitido en esta ruta");
    }

    // Nombre de tipo repetido, o eliminar un tipo que usan días del calendario
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorRespuesta> integridad(DataIntegrityViolationException ex) {
        return responder(HttpStatus.CONFLICT,
                "La operación viola la integridad de los datos: el nombre ya existe o el registro está en uso");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> errorNoPrevisto(Exception ex) {
        LOG.error("Error no previsto", ex);
        return responder(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor");
    }

    private ResponseEntity<ErrorRespuesta> responder(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado).body(new ErrorRespuesta(estado.value(), mensaje));
    }
}
