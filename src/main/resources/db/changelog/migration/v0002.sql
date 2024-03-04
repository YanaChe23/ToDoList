alter table users add column email varchar(255);
alter table users add column password varchar;
alter table users add column role varchar;

insert into users values (2000, 'Kotik', 'kotikdior@gmail.com',
                          '$2a$10$r2tbz0dv/nELiNW9/sp/Cu1bnv3jv3Ypaa66kTxGovx0oRpPKNCeK', 'USER');