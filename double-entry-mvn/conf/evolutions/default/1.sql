# --- First database schema

# --- !Ups

create table accountType (
  id                        bigint not null,
  name                      varchar(60),
  constraint pk_accountType primary key (id))
;

create table account (
  id                        bigint not null,
  name                      varchar(120),
  value            			 decimal(20,2),
  accountType_id                bigint,
  constraint pk_account primary key (id))
;

create table transaction (
  id                        bigint not null,
  date					     timestamp,
  description                varchar(250),
  value            			 decimal(20,2),
  fromAccount_id             bigint,
  toAccount_id               bigint,
  constraint pk_transaction primary key (id)) 
;

create sequence accountType_seq start with 1000;

create sequence account_seq start with 1000;

create sequence trx_seq start with 1000;


alter table account add constraint fk_account_accountType_1 foreign key (accountType_id) 
references accountType (id) on delete restrict on update restrict;

create index ix_account_accountType_1 on account (accountType_id);
alter table account alter column name set not null;
alter table account add constraint unique_name unique(name);



alter table transaction add constraint fk_account_transaction1 foreign key (fromAccount_id) 
references account (id) on delete restrict on update restrict;

create index ix_transaction_fromAccount_1 on transaction (fromAccount_id);


alter table transaction add constraint fk_account_transaction2 foreign key (toAccount_id) 
references account (id) on delete restrict on update restrict;

create index ix_transaction_toAccount_2 on transaction (toAccount_id);


# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists accountType;
drop table if exists account;
drop table if exists transaction;


SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists accountType_seq;
drop sequence if exists account_seq;
drop sequence if exists trx_seq;
