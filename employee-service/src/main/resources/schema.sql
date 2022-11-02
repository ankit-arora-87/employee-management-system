create table ems_users (
id varchar(50) not null,
login varchar(50) not null,
name varchar(255) not null,
salary double not null,
start_date timestamp not null,
created_at timestamp not null,
updated_at timestamp not null,
is_imported integer default 0 COMMENT '0 => No, 1 => Yes' not null,
action_by varchar(100) default 'User' COMMENT 'System => By Import Action, User => By Specific User Action' not null,
primary key (id)
);

create index name_idx on ems_users (name);
create index salary_idx on ems_users (salary);
create index start_date_idx on ems_users (start_date);


alter table ems_users
drop constraint if exists login_unique_idx;

alter table ems_users
add constraint login_unique_idx unique (login);