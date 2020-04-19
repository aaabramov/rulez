# --- !Ups

BEGIN;

ALTER TABLE permissions
    ADD COLUMN description VARCHAR(128) NULL;

UPDATE permissions
SET description = name
WHERE description IS NULL;

ALTER TABLE permissions
    ALTER COLUMN description SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE permissions
    DROP COLUMN description;