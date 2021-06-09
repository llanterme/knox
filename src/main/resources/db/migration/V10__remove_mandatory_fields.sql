USE knox_db;

ALTER TABLE screening MODIFY id_number VARCHAR(50);
ALTER TABLE company_staff MODIFY id_number VARCHAR(50);
ALTER TABLE screening MODIFY temperature_image_url VARCHAR(650);




