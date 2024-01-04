CREATE TABLE cars(
vin varchar(20) PRIMARY KEY,
plate_number varchar(30) UNIQUE,
brand varchar(50),
model varchar(50),
production_year int,
km int,
itp_date timestamp,
service_date timestamp,
username varchar(50) NOT NULL,

CONSTRAINT user_cars FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE
);