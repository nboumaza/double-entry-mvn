# --- Sample dataset

# --- !Ups
insert into accountType (id,name) values (  1,'Asset');
insert into accountType (id,name) values (  2,'Liability');
insert into accountType (id,name) values (  3,'Revenue');
insert into accountType (id,name) values (  4,'Expense');

insert into account (id,name,value,accountType_id) values (  1,'Cash-Acct1',1000,1);
insert into account (id,name,value,accountType_id) values (  2,'Cash-Acct2',2000,1);
insert into account (id,name,value,accountType_id) values (  3,'Expense-Acct1',0,4);
insert into account (id,name,value,accountType_id) values (  4,'Expense-Acct2',0,4);



# --- !Downs

delete from transaction
delete from account;
delete from accountType;

