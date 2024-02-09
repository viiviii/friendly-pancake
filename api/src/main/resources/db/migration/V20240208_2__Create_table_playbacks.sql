create table playbacks (
    id              bigserial       primary key,
    content_id      bigserial,
    url    varchar(255)    not null,
    foreign key (content_id) references contents(id)
);
