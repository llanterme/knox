USE knox_db;

ALTER TABLE screening ADD COLUMN user_temperature VARCHAR(50) Not null AFTER user_mobile;
