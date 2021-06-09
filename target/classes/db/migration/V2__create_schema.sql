-- noinspection SqlNoDataSourceInspectionForFile

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS knox_db DEFAULT CHARACTER SET utf8;

USE knox_db;

create table if not exists api
(
    api_id bigint auto_increment primary key,
    api_version varchar(50) null

);

CREATE TABLE IF NOT EXISTS company (

    company_id BIGINT AUTO_INCREMENT primary key,
    password varchar(50) not null,
    email_address varchar(50) not null,
    mobile_number varchar(50) not null,
    company_name varchar(50) not null,
    company_address varchar(50) null,
    company_identifier varchar(50) not null,
    company_contact_name varchar(50) not null,
    number_validate BIGINT null

);

CREATE TABLE IF NOT EXISTS otp (

    otp_id BIGINT AUTO_INCREMENT primary key,
    code BIGINT not null,
    time_received varchar(50) not null,
    company_id BIGINT not null

);


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

