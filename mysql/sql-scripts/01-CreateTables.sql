CREATE TABLE IF NOT EXISTS person (
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(254) NOT NULL,
    first_name VARCHAR(35) NOT NULL,
    last_Name VARCHAR(35) NOT NULL,
    birthday DATE NOT NULL,
    password_hash BINARY(60) NOT NULL,
    version INT
);