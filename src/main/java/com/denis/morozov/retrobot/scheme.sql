ALTER TABLE Retros_Users DROP CONSTRAINT FK_Retros_Users;
ALTER TABLE Messages DROP CONSTRAINT FK_MessageRetro;
ALTER TABLE Messages DROP CONSTRAINT PK_Messages_PrimaryKey;
ALTER TABLE Retros DROP CONSTRAINT PK_Retros_PrimaryKey;
DROP TABLE Retros_Users;
DROP TABLE Messages;
DROP TABLE Retros;

CREATE TABLE Retros(
  identifier varchar(36) NOT NULL,
  name nvarchar(128) NOT NULL,
  user_id BIGINT NOT NULL,
  deleted int NOT NULL
);

CREATE TABLE Messages(
  identifier varchar(36) NOT NULL,
  text nvarchar(2048) NOT NULL,
  user_id BIGINT NOT NULL,
  retro_identifier varchar(36) NOT NULL
);

CREATE TABLE Retros_Users(
  retro_identifier varchar(36) NOT NULL,
  user_id BIGINT NOT NULL
);

ALTER TABLE Retros
ADD CONSTRAINT PK_Retros_PrimaryKey
PRIMARY KEY NONCLUSTERED (identifier);

ALTER TABLE Messages
ADD CONSTRAINT PK_Messages_PrimaryKey
PRIMARY KEY NONCLUSTERED (identifier);

ALTER TABLE Messages
ADD CONSTRAINT FK_MessageRetro
FOREIGN KEY (retro_identifier)
REFERENCES Retros (identifier);

ALTER TABLE Retros_Users
ADD CONSTRAINT FK_Retros_Users
FOREIGN KEY (retro_identifier)
REFERENCES Retros (identifier);
