# Taller 1 - Diagramas de Arquitectura

Arquitectura de Software 2 - Instituto Tecnológico Metropolitano

## Integrantes

- David Stiven Franco Lopez
- Simon Pulgarin Mejia
- Harol Stiven Restrepo Restrepo
- Mauricio Agudelo Jiménez
- Juan Sebastian Echeverri Gallego

## Primera entrega (API Express JS + BD MongoDB)

Modelado de la **API Festivos** con arquitectura por capas. Carpeta [primera-entrega](primera-entrega/).

| Diagrama | Archivo |
|---|---|
| Diagrama objetual del modelo de datos | [diagrama-objetual-api-festivos.md](primera-entrega/diagrama-objetual-api-festivos.md) |
| Diagrama de arquitectura por capas | [diagrama-arquitectura-api-festivos.md](primera-entrega/diagrama-arquitectura-api-festivos.md) |

Script de `mongosh` que carga la colección `tipos` con los datos para calcular los
festivos: [BDFestivos.mjs](primera-entrega/BDFestivos.mjs)

## Segunda entrega (API Springboot + BD Postgres)

Modelado de la **API Calendario** con arquitectura onion. Carpeta [segunda-entrega](segunda-entrega/).

La API es cliente de la API Festivos: obtiene la lista de festivos de un año y con ella
genera y almacena la clasificación de cada día del año (día laboral, fin de semana o día
festivo). Expone dos operaciones: `GET /api/calendario/generar/{anio}` y
`GET /api/calendario/listar/{anio}`.

| Diagrama | Archivo |
|---|---|
| Diagrama relacional del modelo de datos | [diagrama-relacional-api-calendario.md](segunda-entrega/diagrama-relacional-api-calendario.md) |
| Diagrama de arquitectura onion | [diagrama-arquitectura-api-calendario.md](segunda-entrega/diagrama-arquitectura-api-calendario.md) |

## Tercera entrega (API Festivos: API ExpressJS + BD MongoDB)

Implementación de la **API Festivos** modelada en la primera entrega, con arquitectura por
capas sobre la colección `tipos` de MongoDB. Carpeta [tercer-entrega](tercer-entrega/).

Incluye el CRUD de los festivos, la verificación de una fecha festiva y el listado de los
festivos de un año que consume la API Calendario. Las instrucciones de ejecución y las
operaciones están en su [README](tercer-entrega/README.md), y la documentación interactiva
queda en Swagger UI (`/api-docs`).

## Cuarta entrega (API Calendario: API Springboot + BD PostgreSQL)

Implementación de la **API Calendario** modelada en la segunda entrega, con arquitectura
onion multimódulo (dominio, core, aplicacion, infraestructura y presentacion) sobre las
tablas `Tipo` y `Calendario` de PostgreSQL. Carpeta [cuarta-entrega](cuarta-entrega/).

Genera y lista el calendario laboral de un año consumiendo la API Festivos de la tercera
entrega, e incluye el CRUD de los tipos de día. Las instrucciones de ejecución y las
operaciones están en su [README](cuarta-entrega/README.md), y la documentación interactiva
queda en Swagger UI (`/swagger-ui.html`).

## Quinta entrega (Contenerización con Docker)

Despliegue en contenedores Docker de la **API Festivos** y la **API Calendario**, cada una con
su base de datos (MongoDB y PostgreSQL). Carpeta [quinta-entrega](quinta-entrega/).

Los cuatro contenedores se orquestan con un `docker-compose.yml` sobre una red común, en la
que la API Calendario llama a la API Festivos por el nombre de su contenedor. La carpeta es
autocontenida: trae el código de las dos APIs, sus Dockerfiles y los scripts de las bases de
datos, y todo se levanta con `docker compose up --build`. Las instrucciones, el diagrama de
despliegue y la configuración están en su [README](quinta-entrega/README.md).

Los diagramas de la primera, segunda y quinta entrega están escritos en Mermaid y se renderizan directamente en GitHub.
