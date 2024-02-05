create sequence user_id_gen
start with 1
increment by 1;

create sequence task_id_gen
    start with 1
    increment by 1;

create table users (
    id int primary key unique default nextval('user_id_gen'),
    name text
);

create table tasks (
    id int primary key unique default nextval('task_id_gen'),
    user_id int not null references users(id),
    description text not null,
    deadline varchar(255)
);