USE knox_db;

ALTER TABLE screening ADD COLUMN id_number varchar(50) Not null AFTER user_mobile;
ALTER TABLE company_staff ADD COLUMN id_number varchar(50) Not null AFTER user_mobile;
ALTER TABLE company ADD COLUMN account_locked varchar(50) Not null AFTER number_validate;
