# Imagen de la API Calendario (Spring Boot), construida en dos etapas

# --- Etapa 1: compilar el proyecto multimódulo con Maven y JDK 17 ---
FROM maven:3.9-eclipse-temurin-17 AS compilacion

WORKDIR /fuente

# pom.xml padre y los 5 módulos de la arquitectura onion
# (lo que excluye .dockerignore, como target/, no se copia)
COPY . .

# Se usa el mvn de la imagen y no ./mvnw: en Windows git puede dejar mvnw con fin de
# línea CRLF y el script no correría en Linux. Las pruebas se ejecutan aparte (mvnw package)
RUN mvn -B clean package -DskipTests

# --- Etapa 2: imagen final, solo con el entorno de ejecución de Java y el .jar ---
# eclipse-temurin reemplaza a openjdk:17-jdk-alpine, que ya no existe en Docker Hub.
# Se usa el JRE porque para ejecutar el .jar no hace falta el compilador
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# El módulo presentacion genera el .jar ejecutable con finalName apicalendario
COPY --from=compilacion /fuente/presentacion/target/apicalendario.jar app.jar

ENV PUERTO=8080

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
