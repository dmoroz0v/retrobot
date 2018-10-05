CREATE TABLE Retros(
identifier varchar(36) NOT NULL,
name varchar(50) NOT NULL,
user_id int NOT NULL
);

CREATE TABLE Messages(
identifier varchar(36) NOT NULL,
text text NOT NULL,
user_id int NOT NULL,
retro_identifier varchar(36) NOT NULL
);
