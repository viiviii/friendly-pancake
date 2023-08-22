create table contents (
    id          bigserial       primary key,
    description varchar(255)    not null,
    image_url   varchar(255)    not null,
    title       varchar(255)    not null,
    url         varchar(255)    not null,
    watched     boolean         not null default false
);
