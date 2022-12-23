drop table t_book if exists;


create table t_book (
    id bigint auto_increment,
    create_time timestamp,
    update_time timestamp,
    name varchar(255),
    author varchar(200),
    primary key (id)
);

insert into t_book (name, author, create_time, update_time) values ('python', 'zhangsan', now(), now());
insert into t_book (name, author, create_time, update_time) values ('hadoop', 'lisi', now(), now());
insert into t_book (name, author, create_time, update_time) values ('java', 'wangwu', now(), now());