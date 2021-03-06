# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bpcredit (
  id                        integer not null,
  credit                    integer,
  credit_owner_id           integer,
  constraint pk_bpcredit primary key (id))
;

create table blogger (
  id                        integer not null,
  name                      varchar(255),
  description               varchar(255),
  long_description          TEXT,
  blog_image_path           varchar(255),
  published_date            varchar(255),
  tag                       varchar(255),
  constraint pk_blogger primary key (id))
;

create table comment (
  id                        integer not null,
  content                   varchar(255),
  author_id                 integer,
  product_id                integer,
  created_at                timestamp not null,
  constraint pk_comment primary key (id))
;

create table faq (
  id                        integer not null,
  question                  varchar(255),
  answer                    varchar(255),
  constraint pk_faq primary key (id))
;

create table img_path (
  id                        integer not null,
  public_id                 varchar(255),
  image_url                 varchar(255),
  secret_image_url          varchar(255),
  product_id                integer,
  user_image_id             integer,
  constraint pk_img_path primary key (id))
;

create table main_category (
  id                        integer not null,
  name                      varchar(255),
  constraint pk_main_category primary key (id))
;

create table newsletter (
  id                        integer not null,
  search_string             varchar(255),
  created_date              timestamp,
  user_id                   integer,
  constraint pk_newsletter primary key (id))
;

create table private_message (
  id                        integer not null,
  content                   varchar(255),
  sender_id                 integer,
  receiver_id               integer,
  constraint pk_private_message primary key (id))
;

create table product (
  id                        integer not null,
  name                      varchar(255),
  description               varchar(255),
  long_description          varchar(255),
  category_string           varchar(255),
  main_category_id          integer,
  price                     double,
  published_date            varchar(255),
  owner_id                  integer,
  exchange                  varchar(255),
  is_sold                   boolean,
  is_refunding              boolean,
  refund_reason             varchar(255),
  refundable                boolean,
  location                  varchar(255),
  condition                 varchar(255),
  sub_category_id           integer,
  sub_category_string       varchar(255),
  buyer_user_id             integer,
  credit                    integer,
  is_special                boolean,
  expiry_special            timestamp,
  constraint pk_product primary key (id))
;

create table statistics (
  id                        integer not null,
  no_of_clicks              integer,
  no_of_clicks_s            varchar(255),
  no_of_comments            integer,
  is_it_sold                varchar(255),
  is_it_special             varchar(255),
  product_stats_id          integer,
  constraint pk_statistics primary key (id))
;

create table sub_category (
  id                        integer not null,
  name                      varchar(255),
  main_category_id          integer,
  constraint pk_sub_category primary key (id))
;

create table transaction_p (
  id                        integer not null,
  token                     varchar(255),
  seller_comment            varchar(255),
  buyer_comment             varchar(255),
  buyer_value               double,
  buyer_string_value        varchar(255),
  seller_value              double,
  seller_string_value       varchar(255),
  date_transaction          varchar(255),
  product_id                integer,
  constraint pk_transaction_p primary key (id))
;

create table user (
  id                        integer not null,
  username                  varchar(255),
  password                  varchar(255),
  email                     varchar(255),
  is_admin                  boolean,
  is_pik_store              boolean,
  store_name                varchar(255),
  address                   varchar(255),
  city                      varchar(255),
  phone                     varchar(255),
  category_string           varchar(255),
  store_category_id         integer,
  is_protected_admin        boolean,
  positive_review           integer,
  neutral_review            integer,
  negative_review           integer,
  created_date              varchar(255),
  verified                  boolean,
  confirmation              varchar(255),
  email_verified            boolean,
  email_confirmation        varchar(255),
  image_path                varchar(255),
  constraint pk_user primary key (id))
;

create sequence bpcredit_seq;

create sequence blogger_seq;

create sequence comment_seq;

create sequence faq_seq;

create sequence img_path_seq;

create sequence main_category_seq;

create sequence newsletter_seq;

create sequence private_message_seq;

create sequence product_seq;

create sequence statistics_seq;

create sequence sub_category_seq;

create sequence transaction_p_seq;

create sequence user_seq;

alter table bpcredit add constraint fk_bpcredit_creditOwner_1 foreign key (credit_owner_id) references user (id) on delete restrict on update restrict;
create index ix_bpcredit_creditOwner_1 on bpcredit (credit_owner_id);
alter table comment add constraint fk_comment_author_2 foreign key (author_id) references user (id) on delete restrict on update restrict;
create index ix_comment_author_2 on comment (author_id);
alter table comment add constraint fk_comment_product_3 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_comment_product_3 on comment (product_id);
alter table img_path add constraint fk_img_path_product_4 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_img_path_product_4 on img_path (product_id);
alter table img_path add constraint fk_img_path_userImage_5 foreign key (user_image_id) references user (id) on delete restrict on update restrict;
create index ix_img_path_userImage_5 on img_path (user_image_id);
alter table newsletter add constraint fk_newsletter_user_6 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_newsletter_user_6 on newsletter (user_id);
alter table private_message add constraint fk_private_message_sender_7 foreign key (sender_id) references user (id) on delete restrict on update restrict;
create index ix_private_message_sender_7 on private_message (sender_id);
alter table private_message add constraint fk_private_message_receiver_8 foreign key (receiver_id) references user (id) on delete restrict on update restrict;
create index ix_private_message_receiver_8 on private_message (receiver_id);
alter table product add constraint fk_product_mainCategory_9 foreign key (main_category_id) references main_category (id) on delete restrict on update restrict;
create index ix_product_mainCategory_9 on product (main_category_id);
alter table product add constraint fk_product_owner_10 foreign key (owner_id) references user (id) on delete restrict on update restrict;
create index ix_product_owner_10 on product (owner_id);
alter table product add constraint fk_product_subCategory_11 foreign key (sub_category_id) references sub_category (id) on delete restrict on update restrict;
create index ix_product_subCategory_11 on product (sub_category_id);
alter table product add constraint fk_product_buyerUser_12 foreign key (buyer_user_id) references user (id) on delete restrict on update restrict;
create index ix_product_buyerUser_12 on product (buyer_user_id);
alter table statistics add constraint fk_statistics_productStats_13 foreign key (product_stats_id) references product (id) on delete restrict on update restrict;
create index ix_statistics_productStats_13 on statistics (product_stats_id);
alter table sub_category add constraint fk_sub_category_mainCategory_14 foreign key (main_category_id) references main_category (id) on delete restrict on update restrict;
create index ix_sub_category_mainCategory_14 on sub_category (main_category_id);
alter table transaction_p add constraint fk_transaction_p_product_15 foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_transaction_p_product_15 on transaction_p (product_id);
alter table user add constraint fk_user_storeCategory_16 foreign key (store_category_id) references main_category (id) on delete restrict on update restrict;
create index ix_user_storeCategory_16 on user (store_category_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists bpcredit;

drop table if exists blogger;

drop table if exists comment;

drop table if exists faq;

drop table if exists img_path;

drop table if exists main_category;

drop table if exists newsletter;

drop table if exists private_message;

drop table if exists product;

drop table if exists statistics;

drop table if exists sub_category;

drop table if exists transaction_p;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists bpcredit_seq;

drop sequence if exists blogger_seq;

drop sequence if exists comment_seq;

drop sequence if exists faq_seq;

drop sequence if exists img_path_seq;

drop sequence if exists main_category_seq;

drop sequence if exists newsletter_seq;

drop sequence if exists private_message_seq;

drop sequence if exists product_seq;

drop sequence if exists statistics_seq;

drop sequence if exists sub_category_seq;

drop sequence if exists transaction_p_seq;

drop sequence if exists user_seq;

