drop table user_roles;
drop table roles;
drop table users;
drop table items;
drop table orders;
drop table inventory;
drop table tables;

drop table if exists roles;
create table roles
(
  id bigserial,
  name varchar(20) not null unique,
  primary key (id)
);

drop table if exists users;
create table users
(
  username varchar(20),
  email varchar(50) not null unique,
  password varchar(120) not null,
  primary key (username)
);

drop table if exists user_roles;
create table user_roles
(
  user_id varchar(20),
  role_id bigint,
  foreign key (user_id) references users (username),
  foreign key (role_id) references roles (id),
  primary key (user_id, role_id)
);

drop table if exists inventory;
create table inventory
(
  id bigserial,
  item_name varchar(50) not null unique,
  category varchar(20) not null,
  price real not null,
  quantity int not null,
  primary key (id)
);

drop table if exists tables;
create table tables
(
  id bigserial,
  name varchar(20) not null unique,
  is_available boolean not null,
  primary key (id)
);

drop table if exists orders;
create table orders
(
  id bigserial,
  table_id bigint,
  status varchar(20) not null,
  date date not null,
  foreign key (table_id) references tables (id),
  primary key (id)
);

drop table if exists items;
create table items
(
  id bigserial,
  order_id bigint,
  inventory_item_id bigint,
  selected_quantity int not null,
  foreign key (order_id) references orders (id),
  foreign key (inventory_item_id) references inventory (id),
  primary key (id)
);
