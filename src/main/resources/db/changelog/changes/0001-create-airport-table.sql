--liquibase formatted sql

--changeset Ieva:1

CREATE TABLE airport
(
    airport VARCHAR(255) NOT NULL PRIMARY KEY,
    country VARCHAR(255) NOT NULL,
    city    VARCHAR(255) NOT NULL
);