# Imagen de la API Festivos (Node.js + Express)
# Se fija la versión 22 (la del .nvmrc) en lugar de node:latest, para que la imagen
# se construya igual siempre. La variante alpine es más liviana.
FROM node:22-alpine

WORKDIR /app

# Primero solo las dependencias: si el código cambia pero package*.json no,
# Docker reutiliza esta capa y no vuelve a descargar los paquetes
COPY package*.json ./
RUN npm ci --omit=dev

# Código fuente de la API (lo que excluye .dockerignore no se copia)
COPY . .

# Valores por defecto dentro del contenedor. El docker-compose.yml los sobrescribe
ENV PUERTO=3030

EXPOSE 3030

CMD ["node", "app.js"]
