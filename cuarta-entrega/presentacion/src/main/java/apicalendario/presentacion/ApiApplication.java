package apicalendario.presentacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Punto de entrada de la API. Escanea los componentes de todos los módulos
 * (aplicacion, infraestructura y presentacion) y registra las entidades
 * y los repositorios JPA de la infraestructura.
 */
@SpringBootApplication(scanBasePackages = "apicalendario")
@EntityScan(basePackages = "apicalendario.infraestructura.entidades")
@EnableJpaRepositories(basePackages = "apicalendario.infraestructura.repositorios")
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }
}
