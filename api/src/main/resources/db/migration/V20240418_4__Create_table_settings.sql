-- 테이블 생성
create table settings (
    platform varchar(20) primary key,
    disable_at date
);

-- 테이블에 모든 플랫폼을 활성화 상태로 데이터 추가
insert into settings (platform) values ('NETFLIX'), ('DISNEY_PLUS');
