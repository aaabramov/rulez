# --- !Ups

BEGIN;

ALTER TABLE permissions
    ADD COLUMN active BOOLEAN NULL;

UPDATE permissions
SET active = TRUE
WHERE active IS NULL;

ALTER TABLE permissions
    ALTER COLUMN active SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE permissions
    DROP COLUMN active;