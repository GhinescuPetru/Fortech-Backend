DELETE FROM refreshtoken;

ALTER TABLE refreshtoken DROP CONSTRAINT user_refreshtoken_fk;

ALTER TABLE user_roles DROP CONSTRAINT user_roles_fk1;

ALTER TABLE user_roles DROP CONSTRAINT user_roles_fk2;

ALTER TABLE refreshtoken ADD CONSTRAINT user_refreshtoken_fk FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk1 FOREIGN KEY(username) REFERENCES users(username) ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk2 FOREIGN KEY(rolename) REFERENCES roles(name) ON UPDATE CASCADE ON DELETE CASCADE;