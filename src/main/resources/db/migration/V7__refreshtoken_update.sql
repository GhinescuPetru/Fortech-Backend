ALTER TABLE refreshtoken DROP CONSTRAINT refreshtoken_pkey,
                         DROP COLUMN id,
                         ADD CONSTRAINT refreshtoken_pkey PRIMARY KEY(token);