USE knox_db;

create table if not exists screening
(
    screening_id bigint auto_increment primary key,
    company_id varchar(50) not null,
    screen_date varchar(50) not null,
    screen_time varchar(50) not null,
    user_name varchar(50) not null,
    user_mobile varchar(50) not null,
    temperature_image_url varchar(650) not null,
    screening_questions varchar(8000) not null

);


