select * from member;
select * from member_entity_roles;

insert into member (active, email, password) values
    (true,'baris@gmail.com','$2a$10$VUXA22AwgUdbdycmG6hPt.QnBx2h4rdxYXyGnIP57M.9gUrAMl3GO');

insert into member_entity_roles values (1, 1);

update member set email='basaran.baris+1@gmail.com' where email='basaran.baris@gmail.com';
