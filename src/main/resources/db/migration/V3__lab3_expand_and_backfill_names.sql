-- Expand: keep full_name for old application versions while introducing the new columns.

ALTER TABLE customers
    ADD COLUMN first_name VARCHAR(255),
    ADD COLUMN last_name VARCHAR(255);

CREATE FUNCTION sync_customer_names() RETURNS trigger AS $$
BEGIN
    IF NEW.full_name IS NOT NULL
            AND (NEW.first_name IS NULL OR NEW.last_name IS NULL) THEN
        NEW.first_name := split_part(NEW.full_name, ' ', 1);
        NEW.last_name := CASE
            WHEN position(' ' IN NEW.full_name) > 0
                THEN substring(NEW.full_name FROM position(' ' IN NEW.full_name) + 1)
            ELSE ''
        END;
    ELSIF NEW.full_name IS NULL THEN
        NEW.full_name := trim(concat_ws(' ', NEW.first_name, NEW.last_name));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER customers_sync_names
BEFORE INSERT OR UPDATE ON customers
FOR EACH ROW EXECUTE FUNCTION sync_customer_names();

UPDATE customers
SET first_name = split_part(full_name, ' ', 1),
    last_name = CASE
        WHEN position(' ' IN full_name) > 0
            THEN substring(full_name FROM position(' ' IN full_name) + 1)
        ELSE ''
    END;

ALTER TABLE customers
    ALTER COLUMN first_name SET NOT NULL,
    ALTER COLUMN last_name SET NOT NULL;
