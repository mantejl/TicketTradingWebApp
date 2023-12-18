DROP DATABASE IF EXISTS ticket_db; 
CREATE DATABASE ticket_db; 
USE ticket_db; 
CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    balance FLOAT NOT NULL,
    UNIQUE (username)
);

INSERT INTO User (username, password, email, balance) VALUES
('mantej', 'mantej', 'mantej@gmail.com', 3000.00),
('tj', 'tj', 'tj@gmail.com', 3000.00),
('prabhleen', 'bun', 'bunprabhleen@gmail.com', 3000.00);

CREATE TABLE Favorites (
    user_id INT,
    event_id VARCHAR(50),
    PRIMARY KEY (user_id, event_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);


CREATE TABLE Wallet (
    user_id INT,
    event_id VARCHAR(50),
    num_tickets INT NOT NULL,
    PRIMARY KEY (user_id, event_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

ALTER TABLE Wallet ADD cost_tickets FLOAT;