set referential_integrity false;
truncate table contents;
alter table contents alter column id restart with 1;
set referential_integrity true;