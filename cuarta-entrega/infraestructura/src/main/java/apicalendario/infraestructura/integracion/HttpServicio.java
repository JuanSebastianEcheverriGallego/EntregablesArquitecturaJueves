package apicalendario.infraestructura.integracion;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** Cliente HTTP para consumir servicios externos */
@Configuration
public class HttpServicio {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Tiempos máximos para no dejar la solicitud esperando si la otra API no responde
        return builder
                .connectTimeout(Duration.ofSeconds(5))
                .readTimeout(Duration.ofSeconds(15))
                .build();
    }
}
