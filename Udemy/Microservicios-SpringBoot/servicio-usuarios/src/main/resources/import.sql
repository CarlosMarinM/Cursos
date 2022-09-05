INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('carlos', '$2a$10$7UVyZn3264J0RBpl7TC7JOR.jV29syd9n1r9IsE2mbezHcfDJQzli', true, 'Carlos', 'Marin', 'usuario@correo.com');
INSERT INTO usuarios (username, password, enabled, nombre, apellido, email) values ('admin', '$2a$10$FslybcJxh06tkiyqTOQH/OlC1DCXRS9tgveBrTI8QBrIHckm/xWpy', true, 'Arturo', 'Mendez', 'admin@correo.com');

INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (1, 1);
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (2, 2);
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (2, 1);
