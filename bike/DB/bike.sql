CREATE DATABASE bike;

\c bike;

CREATE TABLE
    bike
    (
        id SERIAL PRIMARY KEY,
        contact BOOLEAN NOT NULL,
        email VARCHAR,
        model VARCHAR,
        name VARCHAR,
        phone VARCHAR,
        purchase_date TIMESTAMP,
        purchase_price NUMERIC,
        serial_number VARCHAR
    );

INSERT INTO bike (id, contact, email, model, name, phone, purchase_date, purchase_price)
  VALUES (1, TRUE, 'jeff@bikes.com', 'Globo MTB 29 Full Suspension', 'Jeff Miller', '328-443-5555', '2020-02-16 19:10:25', '1100');
INSERT INTO bike (id, contact, email, model, name, phone, purchase_date, purchase_price)
  VALUES (2, FALSE, 'samantha@bikes.com', 'Globo Carbon Fiber Race Series', 'Samantha Davis', '448-397-5555', '2020-02-16 19:10:25', '1999');
INSERT INTO bike (id, contact, email, model, name, phone, purchase_date, purchase_price)
  VALUES (3, FALSE, 'dave@bikes.com', 'Globo Time Trial Blade', 'Dave Warren', '563-891-5555', '2020-02-16 19:10:25', '2100');

select setval('bike_id_seq',COALESCE((select max(id) + 1 from bike), 1));

--SECURITY !!!!

create table users(
username varchar(50) not null,
nickname varchar(30),
enabled smallint not null default 1,
primary key (username)
);

create table authorities(
username varchar(50) not null,
authority varchar(50) not null,
foreign key(username) references users(username)
);

create unique index ix_auth_username
on authorities (username,authority);

insert into users(username, nickname, enabled)
values ('dee',
'dd',
1);
insert into authorities (username, authority)
values ('dee', 'ROLE_USER');