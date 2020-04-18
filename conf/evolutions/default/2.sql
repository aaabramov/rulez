# --- !Ups

CREATE TABLE rule_sets
(
    id     BIGSERIAL    NOT NULL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    author VARCHAR(128) NOT NULL
);

CREATE TABLE conditions
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    rule_set_id BIGINT       NOT NULL REFERENCES rule_sets (id) ON DELETE CASCADE,
    key         VARCHAR(255) NOT NULL,
    value       VARCHAR(255) NOT NULL
);

CREATE TABLE rules
(
    id          BIGSERIAL    NOT NULL PRIMARY KEY,
    rule_set_id BIGINT       NOT NULL REFERENCES rule_sets (id) ON DELETE CASCADE,
    key         VARCHAR(255) NOT NULL,
    value       VARCHAR(255) NOT NULL
);

# --- !Downs

DROP TABLE IF EXISTS conditions;
DROP TABLE IF EXISTS rules;
DROP TABLE IF EXISTS rule_sets;