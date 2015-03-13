# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table main_category (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_main_category primary key (id))
;

create table product (
  id                        integer not null,
  name                      varchar(255),
  description               varchar(255),
  category_string           varchar(255),
  category_id               integer,
  price                     double,
  published_date            varchar(255),
  owner_id                  integer,
  availability              varchar(255),
  constraint pk_product primary key (id))
;

create table user (
  id                        integer not null,
  username                  varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  is_admin                  boolean,
  created_date              varchar(255),
  verified                  boolean,
  confirmation              varchar(255),
  constraint pk_user primary key (id))
;

create sequence main_category_seq;

create sequence product_seq;

create sequence user_seq;

alter table product add constraint fk_product_category_1 foreign key (category_id) references main_category (id) on delete restrict on update restrict;
create index ix_product_category_1 on product (category_id);
alter table product add constraint fk_product_owner_2 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_product_owner_2 on product (owner_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists main_category;

drop table if exists product;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists main_category_seq;

drop sequence if exists product_seq;

drop sequence if exists user_seq;

