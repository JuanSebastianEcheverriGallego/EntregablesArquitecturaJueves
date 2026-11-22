package apicalendario.infraestructura.integracion;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import apicalendario.core.interfaces.integracion.IFestivoServicioExterno;
import apicalendario.dominio.DTOs.FestivoDto;

/**
 * Consume la API Festivos. La URL se configura en application.properties
 * (servicioexterno.festivos.url).
 */
@Service
public class FestivoServicioExterno implements IFestivoServicioExterno {

    private final RestTemplate restTemplate;
    private final String url;

    public FestivoServicioExterno(RestTemplate restTemplate,
            @Value("${servicioexterno.festivos.url}") String url) {
        this.restTemplate = restTemplate;
        this.url = url;
    }

    @Override
    public List<FestivoDto> obtenerFestivos(int anio) {
        FestivoDto[] festivos = restTemplate.getForObject(url + "/api/festivos/obtener/{anio}",
                FestivoDto[].class, anio);
        if (festivos == null) {
            throw new IllegalStateException("La API Festivos no retornó la lista de festivos del año " + anio);
        }
        return Arrays.asList(festivos);
    }
}
