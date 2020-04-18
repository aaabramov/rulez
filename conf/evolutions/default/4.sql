# --- !Ups

BEGIN;

ALTER TABLE users
    ADD COLUMN updated_at TIMESTAMP NULL;

UPDATE users
SET updated_at = now()
WHERE updated_at IS NULL;

ALTER TABLE users
    ALTER COLUMN updated_at SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE users
    DROP COLUMN updated_at;