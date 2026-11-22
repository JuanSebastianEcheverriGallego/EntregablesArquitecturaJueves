package apicalendario.core.interfaces.integracion;

import java.util.List;

import apicalendario.dominio.DTOs.FestivoDto;

/**
 * Contrato con la API Festivos. La implementación (llamada HTTP) está en la infraestructura.
 */
public interface IFestivoServicioExterno {

    public List<FestivoDto> obtenerFestivos(int anio);
}
