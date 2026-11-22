--Cambiar la conexión
\c calendario

/* Catálogo de tipos de día. La API asigna los tipos por estos Id */
INSERT INTO Tipo(Id, Tipo) VALUES
	(1, 'Día laboral'),
	(2, 'Fin de semana'),
	(3, 'Día festivo');

/* Continuar la secuencia después de los Id insertados,
	para los tipos que se agreguen desde la API */
SELECT setval(pg_get_serial_sequence('tipo', 'id'), (SELECT MAX(Id) FROM Tipo));
