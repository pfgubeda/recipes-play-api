# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

-- apply changes
create table ingredient (
  id                            bigint generated by default as identity not null,
  name                          varchar(255),
  parent_recipe_id              bigint,
  constraint pk_ingredient primary key (id)
);

create table recipe (
  id                            bigint generated by default as identity not null,
  name                          varchar(255),
  preparation_time              integer not null,
  difficulty                    integer not null,
  constraint pk_recipe primary key (id)
);

create table user (
  id                            bigint generated by default as identity not null,
  name                          varchar(255),
  age                           integer,
  avatar_id                     bigint,
  constraint uq_user_avatar_id unique (avatar_id),
  constraint pk_user primary key (id)
);

create table user_avatar (
  id                            bigint generated by default as identity not null,
  url                           varchar(255),
  constraint pk_user_avatar primary key (id)
);

create table user_password (
  id                            bigint generated by default as identity not null,
  parent_user_id                bigint,
  password_hash                 varchar(255),
  constraint pk_user_password primary key (id)
);

-- foreign keys and indices
create index ix_ingredient_parent_recipe_id on ingredient (parent_recipe_id);
alter table ingredient add constraint fk_ingredient_parent_recipe_id foreign key (parent_recipe_id) references recipe (id) on delete restrict on update restrict;

alter table user add constraint fk_user_avatar_id foreign key (avatar_id) references user_avatar (id) on delete restrict on update restrict;

create index ix_user_password_parent_user_id on user_password (parent_user_id);
alter table user_password add constraint fk_user_password_parent_user_id foreign key (parent_user_id) references user (id) on delete restrict on update restrict;


# --- !Downs

-- drop all foreign keys
alter table ingredient drop constraint if exists fk_ingredient_parent_recipe_id;
drop index if exists ix_ingredient_parent_recipe_id;

alter table user drop constraint if exists fk_user_avatar_id;

alter table user_password drop constraint if exists fk_user_password_parent_user_id;
drop index if exists ix_user_password_parent_user_id;

-- drop all
drop table if exists ingredient;

drop table if exists recipe;

drop table if exists user;

drop table if exists user_avatar;

drop table if exists user_password;

