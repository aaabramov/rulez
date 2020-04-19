# --- !Ups

BEGIN;

ALTER TABLE permissions
    ADD COLUMN updated_at TIMESTAMP NULL;

UPDATE permissions
SET updated_at = now()
WHERE updated_at IS NULL;

ALTER TABLE permissions
    ALTER COLUMN updated_at SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE permissions
    DROP COLUMN updated_at;