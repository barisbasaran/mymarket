
delete from member_entity_roles where member_entity_id=11;
delete from member where id=11;

select * from member;
select * from member where id=45;
select * from member_entity_roles where member_entity_id=45;

update member set password='$2a$10$R3TXpuGDRzKTtAEZ725kFu5k/5UHpcyatur1qDE0gU1pzi32XV5ei';
update member set email='basaran.baris+20@gmail.com' where email='basaran.baris@gmail.com';

update member set first_name='', last_name='' where id=4;
select * from member where id=4;
update address set address_line='';

select * from password_reset;
delete from password_reset;

select * from verify_email;
delete from verify_email;

delete from member_entity_roles;
insert into member_entity_roles values (1, 0);
insert into member_entity_roles values (2, 0);
insert into member_entity_roles values (3, 0);
insert into member_entity_roles values (4, 1);
insert into member_entity_roles values (5, 0);
insert into member_entity_roles values (6, 0);
insert into member_entity_roles values (7, 0);

insert into member (id,active, email, phone, name)
values (1, true, 'ali@yahoo.com', '53234223367', 'Ali Yıldız');

insert into member (id,active, email, phone, name)
values (2, true, 'veli@yahoo.com', '5413445678', 'Veli Özdemir');

insert into member (id,active, email, phone, name)
values (3, true, 'osman@yahoo.com', '5510323367', 'Osman Yıldırım');

update member set address_id=3 where id=1;

select * from address;

delete from address;

drop table product_image;
drop table product;
alter table if exists product add column active boolean not null default true;

select * from product;
select * from product_image;

select * from product_image where product_id=45;

select column_name, data_type, character_maximum_length, column_default, is_nullable
from INFORMATION_SCHEMA.COLUMNS where table_name = 'product';

delete from product_image;
delete from product;

insert into product (id, product_category_id, name, description)
values (1, 1, 'my prodcut 1', 'desc desc');
insert into product_image (id, url, product_id)
values (1, '/uploads/1/1.webp', 1);
insert into product_image (id, url, product_id)
values (2, '/uploads/1/2.webp', 1);

SELECT last_value FROM product_id_seq;
SELECT setval('product_id_seq', 5, true);

SELECT last_value FROM product_image_id_seq;
SELECT setval('product_image_id_seq', 6, true);

select * from product_category;

select * from product_image where product_id=4;

select * from address;
delete from address where id=7;

select * from member;
update member set address_id=null where id=1;
update member set active=true where id=4;

SELECT last_value FROM member_id_seq;
SELECT setval('member_id_seq', 3, true);

select * from country;
select * from city where country_id=11;
delete from city where country_id=14;
delete from country where id=11;

select * from city order by id asc;
update country set has_state=true where name='US';

delete from country where id=4;
delete from city where country_id=4;

SELECT last_value FROM country_id_seq;
SELECT setval('country_id_seq', 2, true);

SELECT last_value FROM city_id_seq;
SELECT setval('city_id_seq', 417, true);

select * from orders;
select * from orders_item;
select * from shipment;

update orders set
    name='09d90141-5037-4298-94a4-13861e217d1c'
where id=3;

select * from product where id=34;

delete from orders;
delete from orders_item;

select* from shipment order by id;

select * from email;
delete from email;

select * from orders order by id desc;
select* from shipment order by id desc;

select * from country;
select * from state;

update country set name='country.turkey' where name='Türkiye';
update country set name='country.sweden' where name='Sweden';
update country set name='country.germany' where name='Germany';
update country set name='country.italy' where name='Italy';
update country set name='country.azerbaijan' where name='Azerbaijan';
update country set name='country.us' where name='US';

select * from shipment where state_id is not null;
select * from address where state_id is not null;
select count(*) from address where state_id is  null;

select * from member where address_id=14;

select * from product_category;
update product_category set parent_id=24 where id=22;
update product_category set parent_id=24 where id=23;

select * from email;

select * from review;

update product set store_id=1;

update store set member_id=45;
select * from store;

select id, store_id from product;

update product_image set cover_image=false where id=185;;
select * from product_image where id=185;

