insert into roles(name) values('ROLE_EMPLOYEE');
insert into roles(name) values('ROLE_MANAGER');
insert into roles(name) values('ROLE_ADMIN');
insert into roles(name) values('ROLE_TEST');

insert into users values('peter', 'peter@peter.com', '$2a$10$Jlg0bNKwqvccB2rqCPreNOult32EUG3/ZX009h2Jtc0IXMa6KVPuq');
insert into users values('jim', 'jim@jim.com', '$2a$10$avyHJk9RsT4D/LfAjyuZpusnwuGAFcFixB1Dg7b.uUPypcr1OINQq');
insert into users values('athena', 'athena@athena.com', '$2a$10$sY0AQ5XRYwBUOjBh9vTmjuEdjjLmNc.HuK0XupZQg9M3NOhRr0VIO');
insert into users values('timmy', 'timmy@timmy.com', '$2a$10$sA8PysYVnflLEKXc7KKeB.pUJKr2Zhvgt.JhT9HjXsp/a7UKRGd4u');

insert into user_roles values('peter', 3);
insert into user_roles values('jim', 2);
insert into user_roles values('athena', 1);
insert into user_roles values('timmy', 2);
insert into user_roles values('timmy', 1);

insert into inventory(category, item_name, price, quantity) values('Coffee', 'Espresso', 2.50, 10);
insert into inventory(category, item_name, price, quantity) values('Drinks', 'Beer', 4.00, 15);
insert into inventory(category, item_name, price, quantity) values('Snacks', 'Toast', 3.50, 5);

insert into tables(name, is_available) values('beach1', true);
insert into tables(name, is_available) values('beach2', false);
insert into tables(name, is_available) values('bar1', true);
insert into tables(name, is_available) values('bar2', true);

insert into orders(status, date, table_id) values('COMPLETE', '2022-09-01', 1);
insert into orders(status, date, table_id) values('READY', '2022-09-01', 2);
insert into orders(status, date, table_id) values('ACTIVE', '2022-09-01', 3);

insert into items(selected_quantity, inventory_item_id, order_id) values(1, 1, 1);
insert into items(selected_quantity, inventory_item_id, order_id) values(2, 2, 2);
insert into items(selected_quantity, inventory_item_id, order_id) values(3, 3, 3);
insert into items(selected_quantity, inventory_item_id, order_id) values(1, 2, 3);
