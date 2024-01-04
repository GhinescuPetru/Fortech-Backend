ALTER TABLE user_roles DROP CONSTRAINT user_roles_fk2,
                       DROP COLUMN id_role,
                       ADD rolename varchar(20) NOT NULL;

ALTER TABLE roles DROP CONSTRAINT roles_pkey,
                  DROP COLUMN id,
                  ADD CONSTRAINT roles_pkey PRIMARY KEY(name);

ALTER TABLE user_roles ADD CONSTRAINT user_roles_fk2 FOREIGN KEY(rolename) REFERENCES roles(name);