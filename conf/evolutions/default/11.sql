# --- !Ups

BEGIN;

ALTER TABLE roles
    ADD COLUMN updated_at  TIMESTAMP    NULL,
    ADD COLUMN created_at  TIMESTAMP    NULL,
    ADD COLUMN active      BOOLEAN      NULL,
    ADD COLUMN description VARCHAR(128) NULL;

UPDATE roles
SET updated_at  = now(),
    created_at  = now(),
    active      = TRUE,
    description = name
WHERE updated_at IS NULL
   OR created_at IS NULL
   OR active IS NULL
   OR description IS NULL;

ALTER TABLE roles
    ALTER COLUMN updated_at SET NOT NULL,
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN active SET NOT NULL,
    ALTER COLUMN description SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE roles
    DROP COLUMN updated_at,
    DROP COLUMN created_at,
    DROP COLUMN active,
    DROP COLUMN description;