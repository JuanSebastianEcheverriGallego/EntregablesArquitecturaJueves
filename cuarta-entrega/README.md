# Cuarta entrega - API Calendario (Spring Boot + PostgreSQL)

Implementación de la **API Calendario** diseñada en la [segunda entrega](../segunda-entrega/):
API RESTful en Spring Boot sobre PostgreSQL con arquitectura onion multimódulo. Genera el
calendario laboral de un año, clasifica cada día como *Día laboral*, *Fin de semana* o *Día
festivo* y lo guarda en la base de datos. Es cliente de la
[API Festivos](../tercer-entrega/), de la que obtiene los festivos del año, por tanto tenemos una
Integración entre las dos APIS

## Requisitos

La API Calendario necesita que la API Festivos esté funcionando, así que se ejecutan cuatro
servicios:

| Servicio | Para qué | Dirección por defecto |
|---|---|---|
| MongoDB | Base de datos de la API Festivos | `localhost:27017` |
| API Festivos (Node.js) | Entrega los festivos del año | `http://localhost:3030` |
| PostgreSQL | Base de datos de la API Calendario | `localhost:5432` |
| API Calendario (Spring Boot) | Esta API | `http://localhost:8080` |

Software necesario:

| Software | Versión | Comprobar con |
|---|---|---|
| JDK (no basta con el JRE: se necesita el compilador `javac`) | 17 o superior | `javac -version` |
| PostgreSQL, con el cliente `psql` | 14 o superior | `psql --version` |
| MongoDB, con el cliente `mongosh` | 6 o superior | `mongosh --version` |
| Node.js | 20.19 o superior | `node -v` |

Maven no se necesita: el proyecto trae el Maven Wrapper (`mvnw` en Linux y macOS,
`mvnw.cmd` en Windows), que lo descarga la primera vez.

## Ejecución

Los servicios se levantan en este orden, desde la raíz del repositorio. Cada paso termina
con una comprobación.

### 1. MongoDB y la base de datos de festivos

Iniciar MongoDB si no está corriendo como servicio:

```bash
# Linux
sudo systemctl start mongod
# Windows: iniciar el servicio "MongoDB Server" desde services.msc
```

Cargar la base de datos `festivos` (solo la primera vez):

```bash
mongosh < primera-entrega/BDFestivos.mjs
```

Comprobar: `mongosh --quiet --eval 'db.getSiblingDB("festivos").tipos.countDocuments()'`
debe responder `4`.

### 2. API Festivos

En otra terminal:

```bash
cd tercer-entrega
npm install
npm start
```

Comprobar: `curl http://localhost:3030/api/festivos/obtener/2023` debe responder la lista de
festivos de 2023. Esta terminal se deja abierta.

### 3. PostgreSQL y la base de datos del calendario

Iniciar PostgreSQL si no está corriendo como servicio:

```bash
# Linux
sudo systemctl start postgresql
# Windows: iniciar el servicio "postgresql-x64-NN" desde services.msc
```

La API se conecta con el usuario `postgres` y la clave `sa`. Si el usuario tiene otra clave,
se puede cambiar (`ALTER USER postgres PASSWORD 'sa';`) o indicar la clave real con la
variable `BD_CLAVE` en el paso 4.

Crear la base de datos `calendario` y cargar el catálogo de tipos (solo la primera vez):

```bash
psql -U postgres -h localhost -f cuarta-entrega/bd/DDL_Calendario.sql
psql -U postgres -h localhost -f cuarta-entrega/bd/DML_Calendario.sql
```

En Linux, si `psql` no acepta la conexión, se pueden ejecutar como el usuario del sistema
`postgres`: `sudo -u postgres psql -f cuarta-entrega/bd/DDL_Calendario.sql` (y lo mismo con
el DML).

Comprobar: `psql -U postgres -h localhost -d calendario -c 'SELECT * FROM tipo;'` debe
mostrar los 3 tipos: *Día laboral*, *Fin de semana* y *Día festivo*.

### 4. API Calendario

En otra terminal, compilar (también ejecuta las pruebas unitarias) e iniciar la API:

```bash
cd cuarta-entrega
./mvnw clean package
java -jar presentacion/target/apicalendario.jar
```

En Windows se compila con `mvnw.cmd clean package`. Para compilar sin ejecutar las pruebas
se agrega `-DskipTests`.

Si algún valor de la [configuración](#configuración) es distinto, se indica con variables
de entorno al iniciar. Por ejemplo, con otra clave de PostgreSQL:

```bash
# Linux y macOS
BD_CLAVE=miclave java -jar presentacion/target/apicalendario.jar
# Windows (PowerShell)
$env:BD_CLAVE="miclave"; java -jar presentacion/target/apicalendario.jar
```

Comprobar: el log termina con `Started ApiApplication` y
`curl http://localhost:8080/api/tipos/listar` responde los 3 tipos.

### 5. Probar el calendario

```bash
curl http://localhost:8080/api/calendario/generar/2023   # true
curl http://localhost:8080/api/calendario/listar/2023    # los 365 días de 2023
```

La documentación interactiva queda en Swagger UI: `http://localhost:8080/swagger-ui.html`.

Si generar responde `false`, lo más común es que la API Festivos no esté corriendo (paso 2).
El motivo exacto queda en el log de la API Calendario.

### Configuración

Los valores por defecto están en
[application.properties](presentacion/src/main/resources/application.properties) y se pueden
sobrescribir con variables de entorno:

| Variable | Valor por defecto | Uso |
|---|---|---|
| `PUERTO` | `8080` | Puerto de la API Calendario |
| `BD_SERVIDOR` | `localhost` | Servidor de PostgreSQL |
| `BD_PUERTO` | `5432` | Puerto de PostgreSQL |
| `BD_BASEDATOS` | `calendario` | Base de datos |
| `BD_USUARIO` / `BD_CLAVE` | `postgres` / `sa` | Credenciales de PostgreSQL |
| `API_FESTIVOS_URL` | `http://localhost:3030` | URL de la API Festivos (propiedad `servicioexterno.festivos.url`) |

## Estructura del proyecto

El proyecto padre ([pom.xml](pom.xml)) agrupa cinco módulos Maven, uno por cada componente
del [diagrama de arquitectura onion](../segunda-entrega/diagrama-arquitectura-api-calendario.md).
Cada módulo solo depende de los anillos interiores:

| Anillo | Módulo | Depende de | Paquetes y clases |
|---|---|---|---|
| Centro | [dominio](dominio/) | Nada (Java puro) | `dominio.entidades`: `Calendario`, `Tipo`. `dominio.DTOs`: `FestivoDto` |
| Centro | [core](core/) | dominio (Java puro) | `core.interfaces.repositorios`: `ICalendarioRepositorio`, `ITipoRepositorio`. `core.interfaces.servicios`: `ICalendarioServicio`, `ITipoServicio`. `core.interfaces.integracion`: `IFestivoServicioExterno`. `core.excepciones` |
| Medio | [aplicacion](aplicacion/) | core, Spring | `aplicacion`: `CalendarioServicio`, `TipoServicio` |
| Exterior | [infraestructura](infraestructura/) | core, Spring Data JPA, PostgreSQL | `infraestructura.entidades`: `CalendarioEntidad`, `TipoEntidad` (JPA). `infraestructura.repositorios`: repositorios JPA y su implementación del core. `infraestructura.mapeadores`: `CalendarioMapeador`, `TipoMapeador`. `infraestructura.integracion`: `HttpServicio`, `FestivoServicioExterno` |
| Exterior | [presentacion](presentacion/) | aplicacion, infraestructura, Spring Web | `presentacion`: `ApiApplication`, `CalendarioControlador`, `TipoControlador`. `presentacion.configuracion`: `SwaggerConfig`. `presentacion.errores`: `ExcepcionesGlobalesHandler`, `ErrorRespuesta` |

Las entidades del dominio no tienen anotaciones de ningún framework. Las anotaciones JPA
están solo en las entidades de la infraestructura, y los mapeadores convierten entre unas y
otras, de modo que hacia el servicio y el controlador solo viajan objetos del dominio.

Las pruebas unitarias ([aplicacion/src/test](aplicacion/src/test/java/apicalendario/aplicacion/))
prueban los servicios con implementaciones en memoria de las interfaces del core, sin base
de datos ni API Festivos.

## Operaciones

### Calendario

| Operación | Método | Ruta | Respuesta |
|---|---|---|---|
| Generar el calendario de un año | GET | `/api/calendario/generar/{anio}` | `true` si se generó y guardó, `false` si la API Festivos no respondió o falló el guardado |
| Listar el calendario de un año | GET | `/api/calendario/listar/{anio}` | Lista de días ordenada por fecha, o lista vacía si el año no se ha generado |

### Tipos de día

CRUD del catálogo de tipos, para administrarlo desde la API sin modificar la base de datos:

| Operación | Método | Ruta | Respuesta |
|---|---|---|---|
| Listar los tipos | GET | `/api/tipos/listar` | 200 con la lista de tipos |
| Obtener un tipo | GET | `/api/tipos/obtener/{id}` | 200, o 404 si no existe |
| Buscar tipos por nombre | GET | `/api/tipos/buscar/{nombre}` | 200 con los tipos cuyo nombre contiene el texto |
| Agregar un tipo | POST | `/api/tipos/agregar` | 201 con el tipo creado (el `id` lo asigna la base de datos) |
| Modificar un tipo | PUT | `/api/tipos/modificar` | 200 con el tipo modificado, o 404 |
| Eliminar un tipo | DELETE | `/api/tipos/eliminar/{id}` | 200 con `true`, 400 si es uno de los tipos 1 a 3, 404 si no existe o 409 si hay días del calendario con ese tipo |

### Errores

| Situación | Respuesta |
|---|---|
| Un parámetro numérico no es un número (por ejemplo `/listar/abc`) | 400 |
| Nombre de tipo vacío, cuerpo JSON mal formado o eliminar uno de los tipos 1 a 3 | 400 |
| Tipo que no existe | 404 |
| Nombre de tipo repetido, o eliminar un tipo en uso | 409 |
| Error no previsto | 500 |

Todos los errores responden con la misma estructura:

```json
{"estado": 404, "mensaje": "No existe el tipo con id=9"}
```

### Ejemplos

Generar y listar el calendario de 2023:

```bash
curl http://localhost:8080/api/calendario/generar/2023
```

```json
true
```

```bash
curl http://localhost:8080/api/calendario/listar/2023
```

```json
[
  {"id":1,"fecha":"2023-01-01","tipo":{"id":3,"tipo":"Día festivo"},"descripcion":"Domingo"},
  {"id":2,"fecha":"2023-01-02","tipo":{"id":1,"tipo":"Día laboral"},"descripcion":"Lunes"},
  ...
]
```

Agregar y modificar un tipo:

```bash
curl -X POST http://localhost:8080/api/tipos/agregar \
  -H 'Content-Type: application/json' -d '{"tipo": "Día cívico"}'

curl -X PUT http://localhost:8080/api/tipos/modificar \
  -H 'Content-Type: application/json' -d '{"id": 4, "tipo": "Jornada cívica"}'
```

## Reglas de negocio

- **Una sola consulta a la API Festivos.** Al generar un año se piden sus festivos una sola
  vez (`GET /api/festivos/obtener/{anio}`), se guardan en memoria y cada día se compara con
  esa lista. Así se evitan 365 llamadas, una por fecha, a `verificar`.
- **Clasificación.** Un día es *Día festivo* si está en la lista de festivos, *Fin de semana*
  si es sábado o domingo, y *Día laboral* en los demás casos. El festivo tiene prioridad: el
  1 de enero de 2023 cae domingo y se clasifica como *Día festivo* con descripción *Domingo*.
- **Descripción.** Es el nombre del día de la semana en español (Lunes a Domingo). No
  depende del idioma configurado en el servidor.
- **Generar de nuevo no duplica.** Antes de guardar se eliminan los días que ya existan para
  ese año. El borrado y el guardado van en una sola transacción: si la API Festivos no
  responde o el guardado falla, el calendario que existía para ese año se conserva. Además,
  la base de datos impide con un índice único que una fecha se guarde dos veces.
- **Ids del catálogo.** La generación asigna los tipos por su `id`: 1 *Día laboral*, 2 *Fin
  de semana* y 3 *Día festivo*. Se pueden renombrar desde la API, pero no eliminar (responde
  400): si se eliminaran, un tipo agregado de nuevo recibiría otro `id` y generar fallaría
  hasta corregir la base de datos a mano. Los demás tipos tampoco se pueden eliminar mientras
  haya días del calendario con ese tipo.

## Cambios frente al diseño de la segunda entrega

- **CRUD de tipos.** El diseño trataba `Tipo` como un catálogo de solo consulta. Se agregaron
  `ITipoServicio`, `TipoServicio` y `TipoControlador` para administrarlo desde la API, y el
  repositorio de tipos quedó con las operaciones listar, obtener, buscar, guardar y eliminar.
  Por eso `Tipo.Id` es `SERIAL` en el script, para que la base de datos asigne el id de los
  tipos nuevos.
- **Repositorio de calendario.** Tiene listar por año, obtener y guardar, más eliminar por
  año, que usa la generación para no duplicar días.
- **Errores de negocio.** Se agregaron las excepciones `EntidadNoEncontradaExcepcion` y
  `DatosNoValidosExcepcion` en el core, que el manejador global convierte en 404 y 400.
