create table playbacks (
    id          bigserial       primary key,
    content_id  bigserial,
    platform    varchar(20),
    url         varchar(255)    not null,

    foreign key (content_id) references contents(id)
);
