USE knox_db;

create table if not exists screening_questions
(
    question_id bigint auto_increment primary key,
    question varchar(50) null

);


