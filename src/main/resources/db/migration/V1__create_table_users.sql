CREATE TABLE users(
id SERIAL PRIMARY KEY,
username varchar(30) UNIQUE NOT NULL,
first_name varchar(30),
last_name varchar(30),
phone_number varchar(20) UNIQUE,
email varchar(40) UNIQUE,
password varchar(120) NOT NULL
);