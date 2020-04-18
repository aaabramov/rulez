# --- !Ups

CREATE TABLE secrets
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    uuid        UUID      NOT NULL UNIQUE,
    data        TEXT      NOT NULL,
    valid_till  TIMESTAMP,
    is_one_time BOOLEAN DEFAULT FALSE,
    viewed      BOOLEAN DEFAULT FALSE
);

CREATE TABLE users
(
    id         SERIAL       NOT NULL PRIMARY KEY,
    uuid       UUID         NOT NULL UNIQUE,
    email      VARCHAR(128) NOT NULL UNIQUE,
    password   VARCHAR(64)  NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS secrets;