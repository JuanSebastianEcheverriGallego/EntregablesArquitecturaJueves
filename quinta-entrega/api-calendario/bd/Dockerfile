# Imagen de la base de datos de la API Calendario (PostgreSQL con las tablas y el catálogo)
FROM postgres:16

# La imagen oficial ejecuta los .sql de /docker-entrypoint-initdb.d en orden alfabético,
# la primera vez que arranca con el volumen de datos vacío. El prefijo numérico asegura
# que el DDL (crea la base de datos calendario y las tablas) corra antes que el DML
COPY DDL_Calendario.sql /docker-entrypoint-initdb.d/01_DDL_Calendario.sql
COPY DML_Calendario.sql /docker-entrypoint-initdb.d/02_DML_Calendario.sql

# Si los scripts se clonaron en Windows pueden traer fin de línea CRLF, y psql tomaría
# el \r como parte del nombre en "\c calendario". Se dejan con fin de línea LF
RUN sed -i 's/\r$//' /docker-entrypoint-initdb.d/*.sql
