CREATE TABLE itps(
                         username varchar(30) UNIQUE PRIMARY KEY NOT NULL,
                         email varchar(40) UNIQUE,
                         password varchar(120) NOT NULL,
                         phone_number varchar(20) UNIQUE,
                         location varchar(60) NOT NULL,
                         rating varchar(20) NOT NULL
);