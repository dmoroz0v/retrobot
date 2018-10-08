CREATE TABLE Retros(
identifier varchar(36) NOT NULL,
name varchar(50) NOT NULL,
user_id int NOT NULL,
deleted int NOT NULL
);

CREATE TABLE Messages(
identifier varchar(36) NOT NULL,
text text NOT NULL,
user_id int NOT NULL,
retro_identifier varchar(36) NOT NULL
);

CREATE TABLE Retros_Users(
retro_identifier varchar(36) NOT NULL,
user_id int NOT NULL
);

ALTER TABLE Retros ADD CONSTRAINT PK_Retros_PrimaryKey PRIMARY KEY NONCLUSTERED (identifier);
ALTER TABLE Messages ADD CONSTRAINT PK_Messages_PrimaryKey PRIMARY KEY NONCLUSTERED (identifier);

ALTER TABLE Messages
ADD CONSTRAINT FK_MessageRetro FOREIGN KEY (retro_identifier)
REFERENCES Retros (identifier);

ALTER TABLE Retros_Users
ADD CONSTRAINT FK_Retros_Users FOREIGN KEY (retro_identifier)
REFERENCES Retros (identifier);
