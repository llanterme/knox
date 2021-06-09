USE knox_db;

ALTER TABLE screening_questions ADD COLUMN question_identifier varchar(50) Not null AFTER question;
ALTER TABLE screening_questions MODIFY COLUMN question VARCHAR(8000);
