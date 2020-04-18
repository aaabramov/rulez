# --- !Ups

BEGIN;

ALTER TABLE rule_sets
    ADD COLUMN created_at TIMESTAMP NULL,
    ADD COLUMN updated_at TIMESTAMP NULL;

UPDATE rule_sets
SET created_at = now(),
    updated_at = now()
WHERE created_at IS NULL
   OR updated_at IS NULL;

ALTER TABLE rule_sets
    ALTER COLUMN created_at SET NOT NULL,
    ALTER COLUMN updated_at SET NOT NULL;

COMMIT;

# --- !Downs

ALTER TABLE rule_sets
    DROP COLUMN created_at,
    DROP COLUMN updated_at;