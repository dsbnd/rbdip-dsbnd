-- Contract: all readers now use first_name/last_name, so the legacy column can go.

DROP TRIGGER customers_sync_names ON customers;
DROP FUNCTION sync_customer_names();
ALTER TABLE customers DROP COLUMN full_name;
