DELETE FROM users;

ALTER TABLE refreshtoken DROP CONSTRAINT user_refreshtoken_fk,
                         DROP COLUMN id_user,
                         ADD username varchar(30) NOT NULL;

ALTER TABLE user_roles DROP CONSTRAINT user_roles_fk1,
                       DROP COLUMN id_user,
                       ADD username varchar(30) NOT NULL;

ALTER TABLE users DROP CONSTRAINT users_pkey,
                  DROP COLUMN id,
                  ADD CONSTRAINT users_pkey PRIMARY KEY(username);

ALTER TABLE refreshtoken ADD CONSTRAINT user_refreshtoken_fk FOREIGN KEY(username) REFERENCES users(username);

ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk1 FOREIGN KEY(username) REFERENCES users(username);