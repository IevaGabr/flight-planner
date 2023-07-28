--liquibase formatted sql

--changeset Ieva:2

CREATE TABLE flight
(
    id             serial PRIMARY KEY,
    airport_from   VARCHAR(255) NOT NULL,
    airport_to     VARCHAR(255) NOT NULL,
    carrier        VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP,
    arrival_time   TIMESTAMP,
    FOREIGN KEY (airport_from) REFERENCES airport (airport) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (airport_to) REFERENCES airport (airport) ON DELETE CASCADE ON UPDATE CASCADE

);