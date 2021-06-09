USE knox_db;

create table if not exists company_staff
(
    company_staff_id bigint auto_increment primary key,
    company_id varchar(50) not null,
    user_name varchar(50) not null,
    user_mobile varchar(50) not null

);

ALTER TABLE screening ADD COLUMN staff varchar(50) Not null AFTER company_id;
