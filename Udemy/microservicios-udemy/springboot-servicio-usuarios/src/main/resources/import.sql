INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('carlos', '$2a$10$mwwIuFJKM/UUUBVGBq5NIeRLaWoOmUqpHU8UllyoXaoxMLxCVmvbK', true, 'Carlos', 'Marin', 'usuario@correo.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('admin', '$2a$10$QrZHLeqldAcx.iUuF12OguyEf/7svw7ZWE1cFdYbdftHONsKkc1le', true, 'Arturo', 'Mendez', 'admin@correo.com');

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (1, 1);
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (2, 2);
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (2, 1);
