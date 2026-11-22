CREATE DATABASE calendario;

--Cambiar la conexión
\c calendario

/* Crear tabla TIPO */
CREATE TABLE Tipo(
	Id SERIAL PRIMARY KEY,
	Tipo VARCHAR(100) NOT NULL
	);

/* Crear indice para TIPO
	ordenado por TIPO */
CREATE UNIQUE INDEX ixTipo
	ON Tipo(Tipo);

/* Crear tabla CALENDARIO */
CREATE TABLE Calendario(
	Id SERIAL PRIMARY KEY,
	Fecha DATE NOT NULL,
	IdTipo INT NOT NULL,
	CONSTRAINT fkCalendario_IdTipo FOREIGN KEY (IdTipo)
		REFERENCES Tipo(Id),
	Descripcion VARCHAR(100) NOT NULL
	);

/* Crear indice para CALENDARIO
	ordenado por FECHA: un día se guarda una sola vez */
CREATE UNIQUE INDEX ixCalendario
	ON Calendario(Fecha);
