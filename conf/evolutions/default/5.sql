-- !Ups

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

INSERT INTO roles(id, name)
VALUES (1, 'default');


CREATE TABLE permissions
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL UNIQUE
);

INSERT INTO permissions(id, name)
VALUES (1, 'view'),
       (2, 'edit'),
       (3, 'delete'),
       (4, 'create');


CREATE TABLE role_permissions
(
    role_id       INT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES permissions (id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

INSERT INTO role_permissions
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4);

BEGIN;

ALTER TABLE users
    ADD COLUMN role_id INT NULL REFERENCES roles (id) ON DELETE CASCADE;

UPDATE users
SET role_id = 1
WHERE role_id IS NULL;

ALTER TABLE users
    ALTER COLUMN role_id SET NOT NULL;

COMMIT;

-- !Downs

DROP TABLE role_permissions;
DROP TABLE permissions;
DROP TABLE roles;