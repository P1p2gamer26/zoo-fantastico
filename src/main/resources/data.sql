-- Primero las Zonas (porque las criaturas dependen de ellas)
INSERT INTO zone (nombre, descripcion) VALUES ('Bosque Encantado', 'Zona con árboles bioluminiscentes y clima templado.');
INSERT INTO zone (nombre, descripcion) VALUES ('Abismo Marino', 'Tanques de alta presión para criaturas acuáticas místicas.');
INSERT INTO zone (nombre, descripcion) VALUES ('Picos de Fuego', 'Ambiente volcánico para seres de lava y dragones.');

-- Luego las Criaturas (asumiendo que los IDs de las zonas son 1, 2 y 3)
INSERT INTO creature (name, species, size, danger_level, health_status, zone_id) 
VALUES ('Luna', 'Unicornio Plateado', 1.8, 2, 'Excelente', 1);

INSERT INTO creature (name, species, size, danger_level, health_status, zone_id) 
VALUES ('Kraken Junior', 'Cefalópodo Gigante', 15.5, 9, 'Sediento', 2);

INSERT INTO creature (name, species, size, danger_level, health_status, zone_id) 
VALUES ('Ignis', 'Salamandra de Fuego', 0.5, 5, 'Estable', 3);
