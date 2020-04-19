# --- !Ups

BEGIN;

ALTER TABLE permissions
    ADD COLUMN created_at TIMESTAMP NULL;

UPDATE permissions
SET created_at = now()
WHERE created_at IS NULL;

ALTER TABLE permissions
    ALTER COLUMN created_at SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE permissions
    DROP COLUMN created_at;