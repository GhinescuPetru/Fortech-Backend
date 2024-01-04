CREATE TABLE IF NOT EXISTS refreshtoken(
id SERIAL PRIMARY KEY,
id_user int NOT NULL,
token varchar NOT NULL UNIQUE,
expiry_date timestamp NOT NULL,
CONSTRAINT user_refreshtoken_fk FOREIGN KEY(id_user) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE
);