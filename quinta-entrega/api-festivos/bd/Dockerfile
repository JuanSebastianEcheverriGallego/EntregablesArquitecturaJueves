# Imagen de la base de datos de la API Festivos (MongoDB con los datos cargados)
FROM mongo:8.0

# La imagen oficial ejecuta los .js y .sh de /docker-entrypoint-initdb.d la primera vez
# que arranca con el volumen de datos vacío. Por eso se usa BDFestivos.js
# (con getSiblingDB) y no el .mjs de la primera entrega (con "use", solo interactivo).
COPY BDFestivos.js /docker-entrypoint-initdb.d/01_BDFestivos.js

# Base de datos a la que se conecta mongosh al ejecutar el script
ENV MONGO_INITDB_DATABASE=festivos
