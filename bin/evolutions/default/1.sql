# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table faq (
  id                        integer not null,
  question                  varchar(255),
  answer                    varchar(255),
  constraint pk_faq primary key (id))
;

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
  main_category_id          integer,
  price                     double,
  published_date            varchar(255),
  owner_id                  integer,
  availability              varchar(255),
  sub_category_id           integer,
  sub_category_string       varchar(255),
  constraint pk_product primary key (id))
;

create table sub_category (
  id                        integer not null,
  name                      varchar(255),
  main_category_id          integer,
  constraint pk_sub_category primary key (id))
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
  email_verified            boolean,
  email_confirmation        varchar(255),
  image_path                varchar(255),
  constraint pk_user primary key (id))
;

create sequence faq_seq;

create sequence main_category_seq;

create sequence product_seq;

create sequence sub_category_seq;

create sequence user_seq;

alter table product add constraint fk_product_mainCategory_1 foreign key (main_category_id) references main_category (id) on delete restrict on update restrict;
create index ix_product_mainCategory_1 on product (main_category_id);
alter table product add constraint fk_product_owner_2 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_product_owner_2 on product (owner_id);
alter table product add constraint fk_product_subCategory_3 foreign key (sub_category_id) references sub_category (id) on delete restrict on update restrict;
create index ix_product_subCategory_3 on product (sub_category_id);
alter table sub_category add constraint fk_sub_category_mainCategory_4 foreign key (main_category_id) references main_category (id) on delete restrict on update restrict;
create index ix_sub_category_mainCategory_4 on sub_category (main_category_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists faq;

drop table if exists main_category;

drop table if exists product;

drop table if exists sub_category;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists faq_seq;

drop sequence if exists main_category_seq;

drop sequence if exists product_seq;

drop sequence if exists sub_category_seq;

drop sequence if exists user_seq;

