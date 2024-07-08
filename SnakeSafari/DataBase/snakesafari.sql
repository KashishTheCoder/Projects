CREATE DATABASE snakesafari;
USE snakesafari;
CREATE TABLE highscores(
	id INT AUTO_INCREMENT PRIMARY KEY,
    player VARCHAR(30),
    score INT UNSIGNED
);