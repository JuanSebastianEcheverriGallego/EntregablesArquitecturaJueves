package apicalendario.presentacion.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/** Documentación de la API en Swagger UI: /swagger-ui.html */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiCalendario() {
        return new OpenAPI().info(new Info()
                .title("API Calendario")
                .version("1.0.0")
                .description("Genera y consulta el calendario laboral de un año, clasificando cada día "
                        + "como día laboral, fin de semana o día festivo. Los festivos se obtienen de la API Festivos."));
    }
}
