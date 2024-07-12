-- 테이블 생성
create table bookmarks (
    id              bigserial       primary key,
    content_source  varchar(255)    not null,
    content_id      varchar(255)    not null,
    content_type    varchar(255)    not null,
    record_title    varchar(255)    not null
);
