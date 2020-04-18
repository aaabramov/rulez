# --- !Ups

BEGIN;

ALTER TABLE users
    ADD COLUMN active BOOLEAN NULL;

UPDATE users
SET active = TRUE
WHERE active IS NULL;

ALTER TABLE users
    ALTER COLUMN active SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE users
    DROP COLUMN active;